package ch.zhaw.freelancer4u.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ch.zhaw.freelancer4u.model.Job;
import ch.zhaw.freelancer4u.model.JobCreateDTO;
import ch.zhaw.freelancer4u.model.JobStateAggregation;
import ch.zhaw.freelancer4u.model.JobType;
import ch.zhaw.freelancer4u.repository.JobRepository;

@RestController
@RequestMapping("/api")
public class JobController {
    @Autowired
    JobRepository jobRepository;

    @PostMapping("/job")
    @Secured("ROLE_admin")
    public ResponseEntity<Job> createJob(
            @RequestBody JobCreateDTO jDTO) {
        Job jDAO = new Job(jDTO.getDescription(), jDTO.getEarnings(), jDTO.getJobType());
        Job job = jobRepository.save(jDAO);
        return new ResponseEntity<>(job, HttpStatus.CREATED);
    }

    @GetMapping("/job")
    public ResponseEntity<Page<Job>> getAllJob(
            @RequestParam(required = false) Double min,
            @RequestParam(required = false) JobType type,
            @RequestParam(required = false, defaultValue = "1") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "2") Integer pageSize) {

        Page<Job> jobs;
        if (min == null && type != null) {
            jobs = jobRepository.findByJobType(type, PageRequest.of(pageNumber - 1, pageSize));
        } else if (min != null && type == null) {
            jobs = jobRepository.findByEarningsGreaterThan(min, PageRequest.of(pageNumber - 1, pageSize));
        } else if (min != null && type != null) {
            jobs = jobRepository.findByJobTypeAndEarningsGreaterThan(type, min,
                    PageRequest.of(pageNumber - 1, pageSize));
        } else {
            jobs = jobRepository.findAll(PageRequest.of(pageNumber - 1, pageSize));
        }

        return new ResponseEntity<>(jobs, HttpStatus.OK);
    }

    @GetMapping("/job/aggregation/state")
    public List<JobStateAggregation> getJobStateAggregation() {
        return jobRepository.getJobStateAggregation();
    }

    @DeleteMapping("/job")
    @Secured("ROLE_admin")
    public ResponseEntity<String> deleteAllJobs() {
        jobRepository.deleteAll();
        return ResponseEntity.status(HttpStatus.OK).body("DELETED");
    }

}
