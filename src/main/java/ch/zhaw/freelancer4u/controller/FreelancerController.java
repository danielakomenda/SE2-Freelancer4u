package ch.zhaw.freelancer4u.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    @Secured("ROLE_admin")
    public ResponseEntity<Page<Freelancer>> getAllFreelancer(
        @RequestParam(required = false, defaultValue = "1") Integer pageNumber,
        @RequestParam(required = false, defaultValue = "2") Integer pageSize
    ) {
        Page<Freelancer> allFree = freelancerRepository.findAll(PageRequest.of(pageNumber-1, pageSize));
        return new ResponseEntity<>(allFree, HttpStatus.OK);
    }


    @GetMapping("/freelancer/{id}")
    @Secured("ROLE_admin")
    public ResponseEntity<Freelancer> getFreelancerById(@PathVariable String id) {
    Optional<Freelancer> optFreelancer = freelancerRepository.findById(id);

        if (optFreelancer.isPresent()){
			return new ResponseEntity<Freelancer>(optFreelancer.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<Freelancer>(HttpStatus.NOT_FOUND);
		}
    }
    
}