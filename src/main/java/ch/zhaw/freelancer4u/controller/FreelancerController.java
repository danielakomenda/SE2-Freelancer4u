package ch.zhaw.freelancer4u.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import ch.zhaw.freelancer4u.model.Freelancer;
import ch.zhaw.freelancer4u.model.FreelancerCreateDTO;
import ch.zhaw.freelancer4u.repository.FreelancerRepository;

@RestController
@RequestMapping("/api")
public class FreelancerController {
    @Autowired
    FreelancerRepository freelancerRepository;

    @PostMapping("/freelancer")
    public ResponseEntity<Freelancer> createFreelancer(
            @RequestBody FreelancerCreateDTO fDTO) {
        Freelancer fDAO = new Freelancer(fDTO.getEmail(), fDTO.getName());
        Freelancer f = freelancerRepository.save(fDAO);
        return new ResponseEntity<>(f, HttpStatus.CREATED);
    }

    @GetMapping("/freelancer")
    public ResponseEntity<List<Freelancer>> getAllFreelancer() {
        List<Freelancer> allFree = freelancerRepository.findAll();
        return new ResponseEntity<>(allFree, HttpStatus.OK);
    }


    @GetMapping("/freelancer/{id}")
    public ResponseEntity<Freelancer> getFreelancerById(@PathVariable String id) {
    Optional<Freelancer> optFreelancer = freelancerRepository.findById(id);

        if (optFreelancer.isPresent()){
			return new ResponseEntity<Freelancer>(optFreelancer.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<Freelancer>(HttpStatus.NOT_FOUND);
		}
    }
}