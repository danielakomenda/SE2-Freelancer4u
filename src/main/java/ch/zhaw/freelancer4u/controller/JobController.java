package ch.zhaw.freelancer4u.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ch.zhaw.freelancer4u.model.Job;
import ch.zhaw.freelancer4u.model.JobCreateDTO;
import ch.zhaw.freelancer4u.model.JobType;
import ch.zhaw.freelancer4u.repository.JobRepository;

@RestController
@RequestMapping("/api")
public class JobController {
    @Autowired
    JobRepository jobRepository;

    @PostMapping("/job")
    public ResponseEntity<Job> createJob(
            @RequestBody JobCreateDTO jDTO) {
        Job jDAO = new Job(jDTO.getDescription(), jDTO.getEarnings(), jDTO.getJobType());
        Job job = jobRepository.save(jDAO);
        return new ResponseEntity<>(job, HttpStatus.CREATED);
    }


    @GetMapping("/job")
        public ResponseEntity<List<Job>> getAllJob(
            @RequestParam(required = false) Double min,
            @RequestParam(required = false) JobType type
            ) {
            
                List<Job> jobs;
                if (min != null && type != null) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }

                if (min == null && type == null) {
                    jobs = jobRepository.findAll();
                } else if (min != null) {
                    jobs = jobRepository.findByEarningsGreaterThan(min);
                } else {
                    jobs = jobRepository.findByJobType(type);
                }
                
                return new ResponseEntity<>(jobs, HttpStatus.OK);
            }
}

