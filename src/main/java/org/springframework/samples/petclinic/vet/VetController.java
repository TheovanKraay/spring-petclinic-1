package org.springframework.samples.petclinic.vet;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
class VetController {

	private final VetRepository vetRepository;

	private final SpecialtyRepository specialtyRepository;

	public VetController(VetRepository vetRepository, SpecialtyRepository specialtyRepository) {
		this.vetRepository = vetRepository;
		this.specialtyRepository = specialtyRepository;
	}

	private List<Vet> findAllVetsWithSpecialties() {
		List<Vet> vets = StreamSupport.stream(vetRepository.findAll().spliterator(), false)
			.collect(java.util.stream.Collectors.toList());
		List<Specialty> allSpecialties = StreamSupport.stream(specialtyRepository.findAll().spliterator(), false)
			.collect(java.util.stream.Collectors.toList());
		for (Vet vet : vets) {
			List<Specialty> vetSpecialties = new ArrayList<>();
			for (String specId : vet.getSpecialtyIds()) {
				allSpecialties.stream()
					.filter(s -> s.getId().equals(specId))
					.findFirst()
					.ifPresent(vetSpecialties::add);
			}
			vet.setSpecialties(vetSpecialties);
		}
		return vets;
	}

	@GetMapping("/vets.html")
	public String showVetList(Model model) {
		List<Vet> vets = findAllVetsWithSpecialties();
		model.addAttribute("listVets", vets);
		return "vets/vetList";
	}

	@GetMapping({ "/vets" })
	public @ResponseBody Vets showResourcesVetList() {
		Vets vets = new Vets();
		vets.getVetList().addAll(findAllVetsWithSpecialties());
		return vets;
	}

}

