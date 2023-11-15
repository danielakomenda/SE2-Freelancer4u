package ch.zhaw.freelancer4u.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}