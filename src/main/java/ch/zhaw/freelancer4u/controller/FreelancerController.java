package ch.zhaw.freelancer4u.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ch.zhaw.freelancer4u.model.Freelancer;
import ch.zhaw.freelancer4u.model.FreelancerCreateDTO;
import ch.zhaw.freelancer4u.model.MailInformation;
import ch.zhaw.freelancer4u.repository.FreelancerRepository;
import ch.zhaw.freelancer4u.service.MailValidatorService;

@RestController
@RequestMapping("/api/freelancer")
public class FreelancerController {
    @Autowired
    FreelancerRepository freelancerRepository;

    @Autowired
    MailValidatorService mailValidatorService;
    
    @PostMapping("/create")
    @Secured("ROLE_admin")
    public ResponseEntity<Freelancer> createFreelancer(
            @RequestBody FreelancerCreateDTO fDTO
    ) {
        // Check, if Email is invalid
        MailInformation mailInformation = mailValidatorService.validateEmail(fDTO.getEmail());
        if (mailInformation.isDisposable() || !mailInformation.isDns() || !mailInformation.isFormat()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Check, if Email is already in Database
        Freelancer freelancer = freelancerRepository.findFirstByEmail(fDTO.getEmail());
        if (freelancer != null) {
            return new ResponseEntity<Freelancer>(HttpStatus.CONFLICT);
        }

        // Create Freelancer
        Freelancer fDAO = new Freelancer(fDTO.getEmail(), fDTO.getName());
        Freelancer f = freelancerRepository.save(fDAO);
        return new ResponseEntity<>(f, HttpStatus.CREATED);
    }


    @GetMapping("/getall")
    @Secured("ROLE_admin")
    public ResponseEntity<Page<Freelancer>> getAllFreelancer(
            @RequestParam(required = false, defaultValue = "1") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "2") Integer pageSize
    ) {
        Page<Freelancer> allFree = freelancerRepository.findAll(PageRequest.of(pageNumber - 1, pageSize));
        return new ResponseEntity<>(allFree, HttpStatus.OK);
    }


    @GetMapping("/getalltogether")
    @Secured("ROLE_admin")
    public ResponseEntity<List<Freelancer>> getListOfAllFreelancer() {
        List<Freelancer> allFree = freelancerRepository.findAll();
        if (!allFree.isEmpty()){
        return new ResponseEntity<List<Freelancer>>(allFree, HttpStatus.OK);
        } else {
            return new ResponseEntity<List<Freelancer>>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/id/{id}")
    @Secured("ROLE_admin")
    public ResponseEntity<Freelancer> getFreelancerById(@PathVariable String id) {
        Optional<Freelancer> optFreelancer = freelancerRepository.findById(id);
        if (optFreelancer.isPresent()) {
            return new ResponseEntity<Freelancer>(optFreelancer.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<Freelancer>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search/name")
    @Secured("ROLE_admin")
    public ResponseEntity<List<Freelancer>> getFreelancerByName(@PathVariable String name) {
        List<Freelancer> optFreelancer = freelancerRepository.findFreelancerByName(name);
        if (!optFreelancer.isEmpty()) {
            return new ResponseEntity<List<Freelancer>>(optFreelancer, HttpStatus.OK);
        } else {
            return new ResponseEntity<List<Freelancer>>(HttpStatus.NOT_FOUND);
        }
    }
}