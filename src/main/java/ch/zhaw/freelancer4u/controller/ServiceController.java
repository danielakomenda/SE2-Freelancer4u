package ch.zhaw.freelancer4u.controller;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ch.zhaw.freelancer4u.model.Job;
import ch.zhaw.freelancer4u.model.JobStateChangeDTO;
import ch.zhaw.freelancer4u.service.JobService;

@RestController
@RequestMapping("/api/service")
public class ServiceController {
    @Autowired
    JobService jobService;

    @PutMapping("/assignjob")
    public ResponseEntity<Job> assignJob(@RequestBody JobStateChangeDTO changeS) {
        String freelancerEmail = changeS.getFreelancerEmail();
        String jobId = changeS.getJobId();
        Optional<Job> job = jobService.assignJob(jobId, freelancerEmail);
        if (job.isPresent()) {
            return new ResponseEntity<>(job.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    @PutMapping("/completejob")
    public ResponseEntity<Job> completeJob(@RequestBody JobStateChangeDTO changeS) {
        String freelancerEmail = changeS.getFreelancerEmail();
        String jobId = changeS.getJobId();
        Optional<Job> job = jobService.completeJob(jobId, freelancerEmail);
        if (job.isPresent()) {
            return new ResponseEntity<>(job.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
