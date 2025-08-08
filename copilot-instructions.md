# Convert Spring JPA project to Spring Data Cosmos

## High-level plan
1) Swap build dependencies (remove JPA, add Cosmos + Identity).
2) Add `cosmos` profile and properties.
3) Add Cosmos config with proper Azure identity authentication.
4) Transform entities (ids → `String`, add `@Container` and `@PartitionKey`, remove JPA mappings, adjust relationships).
5) Convert repositories (`JpaRepository` → `CosmosRepository`).
6) **CRITICAL**: Update ALL test files to work with String IDs and Cosmos repositories.
7) Seed data via `CommandLineRunner`.

## Step-by-step

### Step 1 — Build dependencies
- **Maven** (`pom.xml`): 
  - Remove dependency `spring-boot-starter-data-jpa`
  - Remove database-specific dependencies (H2, MySQL, PostgreSQL) unless needed elsewhere
  - Add `com.azure:azure-spring-data-cosmos:5.17.0` (or latest compatible version)
  - Add `com.azure:azure-identity:1.15.4` (required for DefaultAzureCredential)
- **Gradle**: Apply same dependency changes for Gradle syntax
- Remove testcontainers and JPA-specific test dependencies

### Step 2 — Properties and Configuration
- Create `src/main/resources/application-cosmos.properties`:
  ```properties
  azure.cosmos.uri=${COSMOS_URI:https://localhost:8081}
  azure.cosmos.database=${COSMOS_DATABASE:petclinic}
  azure.cosmos.populate-query-metrics=false
  azure.cosmos.enable-multiple-write-locations=false
  ```
- Update `src/main/resources/application.properties`:
  ```properties
  spring.profiles.active=cosmos
  ```

### Step 3 — Configuration class with Azure Identity
- Create `src/main/java/<rootpkg>/config/CosmosConfiguration.java`:
  ```java
  @Configuration
  @EnableCosmosRepositories(basePackages = "<rootpkg>")
  public class CosmosConfiguration extends AbstractCosmosConfiguration {
      
      @Value("${azure.cosmos.uri}")
      private String uri;
      
      @Value("${azure.cosmos.database}")
      private String dbName;
      
      @Bean
      public CosmosClientBuilder getCosmosClientBuilder() {
          return new CosmosClientBuilder()
              .endpoint(uri)
              .credential(new DefaultAzureCredentialBuilder().build());
      }
      
      @Override
      protected String getDatabaseName() {
          return dbName;
      }
      
      @Bean
      public CosmosConfig cosmosConfig() {
          return CosmosConfig.builder()
              .enableQueryMetrics(false)
              .build();
      }
  }
  ```
- **IMPORTANT**: Use `DefaultAzureCredentialBuilder().build()` instead of key-based authentication for production security

### Step 4 — Entity transformation
- Target all classes with JPA annotations (`@Entity`, `@MappedSuperclass`, `@Embeddable`)
- **Base entity changes**:
  - Change `id` field type from `Integer` to `String`
  - Add `@Id` and `@GeneratedValue` annotations
  - Add `@PartitionKey` field (typically `String partitionKey`)
  - Remove all `jakarta.persistence` imports
- **Entity-specific changes**:
  - Replace `@Entity` with `@Container(containerName = "<plural-entity-name>")`
  - Remove `@Table`, `@Column`, `@JoinColumn`, etc.
  - Remove relationship annotations (`@OneToMany`, `@ManyToOne`, `@ManyToMany`)
  - For relationships:
    - Embed collections for one-to-many (e.g., `List<Pet> pets` in Owner)
    - Use reference IDs for many-to-one (e.g., `String ownerId` in Pet)
  - Add constructor to set partition key: `setPartitionKey("entityType")`
- **CRITICAL - Method Signature Conflicts**:
  - **When converting ID types from Integer to String, check for method signature conflicts**
  - **Common conflict**: `getPet(String name)` vs `getPet(String id)` - both have same signature
  - **Solution**: Rename methods to be specific:
    - `getPet(String id)` for ID-based lookup
    - `getPetByName(String name)` for name-based lookup
    - `getPetByName(String name, boolean ignoreNew)` for conditional name-based lookup
  - **Update ALL callers** of renamed methods in controllers and tests
- **Method updates for entities**:
  - Update `addVisit(Integer petId, Visit visit)` to `addVisit(String petId, Visit visit)`
  - Ensure all ID comparison logic uses `.equals()` instead of `==`

### Step 5 — Repository conversion
- Change all repository interfaces:
  - From: `extends JpaRepository<Entity, Integer>`
  - To: `extends CosmosRepository<Entity, String>`
- **Query method updates**:
  - Remove pagination parameters from custom queries
  - Change `Page<Entity> findByX(String param, Pageable pageable)` to `List<Entity> findByX(String param)`
  - Update `@Query` annotations to use Cosmos SQL syntax
  - **Replace custom method names**: `findPetTypes()` → `findAllOrderByName()`
  - **Update ALL references** to changed method names in controllers and formatters

### Step 6 — Data seeding
- Create `@Component` implementing `CommandLineRunner`:
  ```java
  @Component
  public class DataSeeder implements CommandLineRunner {
      @Override
      public void run(String... args) throws Exception {
          if (ownerRepository.count() > 0) {
              return; // Data already exists
          }
          // Seed comprehensive test data with String IDs
          // Use meaningful ID patterns: "owner-1", "pet-1", "pettype-1", etc.
      }
  }
  ```

### Step 7 — Test file conversion (CRITICAL SECTION)
**This step is often overlooked but essential for successful conversion**

#### A. **COMPILATION CHECK STRATEGY**
- **After each major change, run `mvn test-compile` to catch issues early**
- **Fix compilation errors systematically before proceeding**
- **Don't rely on IDE - Maven compilation reveals all issues**

#### B. **Search and Update ALL test files systematically**
**Use search tools to find and update every occurrence:**
- Search for: `int.*TEST.*ID` → Replace with: `String.*TEST.*ID = "test-xyz-1"`
- Search for: `setId\(\d+\)` → Replace with: `setId("test-id-X")`
- Search for: `findById\(\d+\)` → Replace with: `findById("test-id-X")`
- Search for: `\.findPetTypes\(\)` → Replace with: `.findAllOrderByName()`
- Search for: `\.findByLastNameStartingWith\(.*,.*Pageable` → Remove pagination parameter

#### C. Update test annotations and imports
- Replace `@DataJpaTest` with `@SpringBootTest` or appropriate slice test
- Remove `@AutoConfigureTestDatabase` annotations
- Remove `@Transactional` from tests (unless single-partition operations)
- Remove imports from `org.springframework.orm` package

#### D. Fix entity ID usage in ALL test files
**Critical files that MUST be updated (search entire test directory):**
- `*ControllerTests.java` - Path variables, entity creation, mock setup
- `*ServiceTests.java` - Repository interactions, entity IDs
- `EntityUtils.java` - Utility methods for ID handling
- `*FormatterTests.java` - Repository method calls
- `*ValidatorTests.java` - Entity creation with String IDs
- Integration test classes - Test data setup

#### E. **Fix Controller and Service classes affected by repository changes**
- **Update controllers that call repository methods with changed signatures**
- **Update formatters/converters that use repository methods**
- **Common files to check**:
  - `PetTypeFormatter.java` - often calls `findPetTypes()` method
  - `*Controller.java` - may have pagination logic to remove
  - Service classes that use repository methods

#### F. Update repository mocking in tests
- Remove pagination from repository mocks:
  - `given(repository.findByX(param, pageable)).willReturn(pageResult)` 
  - → `given(repository.findByX(param)).willReturn(listResult)`
- Update method names in mocks:
  - `given(petTypeRepository.findPetTypes()).willReturn(types)`
  - → `given(petTypeRepository.findAllOrderByName()).willReturn(types)`

#### G. Fix utility classes used by tests
- Update `EntityUtils.java` or similar:
  - Remove JPA-specific exception imports (`ObjectRetrievalFailureException`)
  - Change method signatures from `int id` to `String id`
  - Update ID comparison logic: `entity.getId() == entityId` → `entity.getId().equals(entityId)`
  - Replace JPA exceptions with standard exceptions (`IllegalArgumentException`)

#### H. Update assertions for String IDs
- Change ID assertions:
  - `assertThat(entity.getId()).isNotZero()` → `assertThat(entity.getId()).isNotEmpty()`
  - `assertThat(entity.getId()).isEqualTo(1)` → `assertThat(entity.getId()).isEqualTo("test-id-1")`
  - JSON path assertions: `jsonPath("$.id").value(1)` → `jsonPath("$.id").value("test-id-1")`

### Step 8 — **Systematic Error Resolution Process**

#### When compilation fails:
1. **Run `mvn compile` first** - fix main source issues before tests
2. **Run `mvn test-compile`** - systematically fix each test compilation error
3. **Focus on most frequent error patterns**:
   - `int cannot be converted to String` → Change test constants and entity setters
   - `method X cannot be applied to given types` → Remove pagination parameters
   - `cannot find symbol: method Y()` → Update to new repository method names
   - Method signature conflicts → Rename conflicting methods

#### Common error patterns and solutions:
- **`method findByLastNameStartingWith cannot be applied`** → Remove `Pageable` parameter
- **`cannot find symbol: method findPetTypes()`** → Change to `findAllOrderByName()`
- **`incompatible types: int cannot be converted to String`** → Update test ID constants
- **`method getPet(String) is already defined`** → Rename one method (e.g., `getPetByName`)
- **`cannot find symbol: method isNotZero()`** → Change to `isNotEmpty()` for String IDs

### Step 9 — Validation checklist
After conversion, verify:
- [ ] **Main application compiles**: `mvn compile` succeeds
- [ ] **All test files compile**: `mvn test-compile` succeeds  
- [ ] **No compilation errors**: Address every single compilation error
- [ ] No remaining `jakarta.persistence` imports
- [ ] All entity IDs are `String` type consistently
- [ ] All repository interfaces extend `CosmosRepository<Entity, String>`
- [ ] Configuration uses `DefaultAzureCredential` for authentication
- [ ] Data seeding component exists and works
- [ ] Test files use String IDs consistently
- [ ] Repository mocks updated for Cosmos methods
- [ ] **No method signature conflicts** in entity classes
- [ ] **All renamed methods updated** in callers (controllers, tests, formatters)

### Common pitfalls to avoid
1. **Not checking compilation frequently** - Run `mvn test-compile` after each major change
2. **Method signature conflicts** - `getPet(String name)` vs `getPet(String id)` cause compilation errors
3. **Forgetting to update method callers** - When renaming `getPet()` to `getPetByName()`, update ALL callers
4. **Missing repository method renames** - `findPetTypes()` must be updated everywhere it's called
5. **Using key-based authentication** - Use `DefaultAzureCredential` instead
6. **Mixing Integer and String IDs** - Be consistent with String IDs everywhere, especially in tests
7. **Not updating controller pagination logic** - Remove pagination from controllers when repositories change
8. **Leaving JPA-specific test annotations** - Replace with Cosmos-compatible alternatives
9. **Incomplete test file updates** - Search entire test directory, not just obvious files

### Debugging compilation issues systematically
If compilation fails after conversion:
1. **Start with main compilation**: `mvn compile` - fix entity and controller issues first
2. **Then test compilation**: `mvn test-compile` - fix each error systematically
3. **Check for remaining `jakarta.persistence` imports** throughout codebase
4. **Verify all test constants use String IDs** - search for `int.*TEST.*ID`
5. **Ensure repository method signatures match** new Cosmos interface
6. **Check for mixed Integer/String ID usage** in entity relationships and tests
7. **Validate all mocking uses correct method names** (`findAllOrderByName()` not `findPetTypes()`)
8. **Look for method signature conflicts** - resolve by renaming conflicting methods
9. **Verify assertion methods work with String IDs** (`isNotEmpty()` not `isNotZero()`)

### **Pro Tips for Success**
- **Compile early and often** - Don't let errors accumulate
- **Use global search and replace** - Find all occurrences of patterns to update
- **Be systematic** - Fix one type of error across all files before moving to next
- **Test method renames carefully** - Ensure all callers are updated
- **Use meaningful String IDs** - "owner-1", "pet-1" instead of random strings
- **Check controller classes** - They often call repository methods that change signatures

This comprehensive guide ensures successful JPA to Cosmos DB conversion with properly functioning tests and no compilation errors.
