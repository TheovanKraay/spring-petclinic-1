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
- **Method updates for entities**:
  - If entity has `getPet(Integer id)` methods, create separate methods:
    - `getPet(String id)` for ID-based lookup
    - `getPetByName(String name)` for name-based lookup

### Step 5 — Repository conversion
- Change all repository interfaces:
  - From: `extends JpaRepository<Entity, Integer>`
  - To: `extends CosmosRepository<Entity, String>`
- **Query method updates**:
  - Remove pagination parameters from custom queries
  - Change `Page<Entity> findByX(String param, Pageable pageable)` to `List<Entity> findByX(String param)`
  - Update `@Query` annotations to use Cosmos SQL syntax
  - Replace method names like `findPetTypes()` with standard `findAll()`

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
          // Seed comprehensive test data here
      }
  }
  ```

### Step 7 — Test file conversion (CRITICAL SECTION)
**This step is often overlooked but essential for successful conversion**

#### A. Update test annotations and imports
- Replace `@DataJpaTest` with `@SpringBootTest` or appropriate slice test
- Remove `@AutoConfigureTestDatabase` annotations
- Remove `@Transactional` from tests (unless single-partition operations)
- Remove imports from `org.springframework.orm` package

#### B. Fix entity ID usage in ALL test files
**Search and replace systematically across all test files:**
- Change all `Integer` ID variables to `String`
- Update test constants: `private static final int TEST_ID = 1` → `private static final String TEST_ID = "test-id-1"`
- Update entity ID setters: `entity.setId(1)` → `entity.setId("test-id-1")`
- Update repository calls: `repository.findById(1)` → `repository.findById("test-id-1")`

#### C. Update repository mocking in tests
- Remove pagination from repository mocks:
  - `given(repository.findByX(param, pageable)).willReturn(pageResult)` 
  - → `given(repository.findByX(param)).willReturn(listResult)`
- Update method names in mocks:
  - `given(petTypeRepository.findPetTypes()).willReturn(types)`
  - → `given(petTypeRepository.findAll()).willReturn(types)`

#### D. Fix utility classes used by tests
- Update `EntityUtils.java` or similar:
  - Remove JPA-specific exception imports (`ObjectRetrievalFailureException`)
  - Change method signatures from `int id` to `String id`
  - Update ID comparison logic: `entity.getId() == entityId` → `entity.getId().equals(entityId)`
  - Replace JPA exceptions with standard exceptions

#### E. Update assertions for String IDs
- Change ID assertions:
  - `assertThat(entity.getId()).isNotZero()` → `assertThat(entity.getId()).isNotEmpty()`
  - `assertThat(entity.getId()).isEqualTo(1)` → `assertThat(entity.getId()).isEqualTo("test-id-1")`

#### F. Files that typically need test updates
**Must check and update these test files:**
- `*ControllerTests.java` - Update path variables and entity creation
- `*ServiceTests.java` - Update repository interactions and entity IDs
- `EntityUtils.java` - Update utility methods for ID handling
- `*FormatterTests.java` - Update repository method calls
- `*ValidatorTests.java` - Update entity creation with String IDs
- Integration test classes - Update test data setup

### Step 8 — Validation checklist
After conversion, verify:
- [ ] Main application compiles without errors
- [ ] All test files compile without errors (`mvn test-compile`)
- [ ] No remaining `jakarta.persistence` imports
- [ ] All entity IDs are `String` type
- [ ] All repository interfaces extend `CosmosRepository<Entity, String>`
- [ ] Configuration uses `DefaultAzureCredential` for authentication
- [ ] Data seeding component exists and works
- [ ] Test files use String IDs consistently
- [ ] Repository mocks updated for Cosmos methods

### Common pitfalls to avoid
1. **Forgetting test file updates** - This causes compilation failures
2. **Using key-based authentication** - Use `DefaultAzureCredential` instead
3. **Mixing Integer and String IDs** - Be consistent with String IDs everywhere
4. **Not updating method calls** - `getPet(int)` vs `getPet(String)` vs `getPetByName(String)`
5. **Leaving JPA-specific test annotations** - Replace with Cosmos-compatible alternatives
6. **Not handling pagination removal** - Update both implementation and tests
7. **Missing partition key setup** - Ensure all entities have partition key logic

### Debugging compilation issues
If compilation fails after conversion:
1. Check for remaining `jakarta.persistence` imports
2. Verify all test constants use String IDs
3. Ensure repository method signatures match new Cosmos interface
4. Check for mixed Integer/String ID usage in entity relationships
5. Validate all test mocking uses correct method names (`findAll()` not `findPetTypes()`)

This comprehensive guide ensures successful JPA to Cosmos DB conversion with properly functioning tests.
