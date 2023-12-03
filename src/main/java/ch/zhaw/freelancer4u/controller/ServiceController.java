package ch.zhaw.freelancer4u.controller;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.zhaw.freelancer4u.model.Freelancer;
import ch.zhaw.freelancer4u.model.Job;
import ch.zhaw.freelancer4u.model.JobState;
import ch.zhaw.freelancer4u.model.JobStateChangeDTO;
import ch.zhaw.freelancer4u.model.Mail;
import ch.zhaw.freelancer4u.repository.FreelancerRepository;
import ch.zhaw.freelancer4u.service.JobService;
import ch.zhaw.freelancer4u.service.MailService;

@RestController
@RequestMapping("/api/")
public class ServiceController {
    private static final Mail mail = new Mail();
    MailService mailService = new MailService();

    @Autowired
    JobService jobService;

    @Autowired
    FreelancerRepository freelancerRepository;

    @PutMapping("/service/assignjob")
    @Secured("ROLE_admin")
    public ResponseEntity<Job> assignJob(@RequestBody JobStateChangeDTO changeS) {
        String freelancerEmail = changeS.getFreelancerEmail();
        String jobId = changeS.getJobId();

        Optional<Job> job = jobService.assignJob(jobId, freelancerEmail);
        JobState state = job.get().getJobState();
        String description = job.get().getDescription();
        String details = job.get().getDetailDescription();
        
        String assignSubject = "Assigned job with status " +state;
        String assignMessage = "Hi, the job " +description +" was marked as " +state +"\nThis job contains the following task: " +details;

        if (job.isPresent()) {
            mail.setSubject(assignSubject);
            mail.setMessage(assignMessage);
            mail.setTo(freelancerEmail);

            if (mailService.sendMail(mail)) {
                return new ResponseEntity<>(job.get(), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/service/completejob")
    @Secured("ROLE_admin")
    public ResponseEntity<Job> completeJob(@RequestBody JobStateChangeDTO changeS) {
        String freelancerEmail = changeS.getFreelancerEmail();
        String jobId = changeS.getJobId();
        Optional<Job> job = jobService.completeJob(jobId, freelancerEmail);

        JobState state = job.get().getJobState();
        String description = job.get().getDescription();
        String details = job.get().getDetailDescription();

        String completeSubject = "Completed job with status " +state;
        String completeMessage = "Hi, the job " + description + " was marked as " + state +"\n all following tasks are finished: " +details;

        if (job.isPresent()) {
            mail.setSubject(completeSubject);
            mail.setMessage(completeMessage);
            mail.setTo(freelancerEmail);

            if (mailService.sendMail(mail)) {
                return new ResponseEntity<>(job.get(), HttpStatus.OK);
            }
            return new ResponseEntity<>(job.get(), HttpStatus.I_AM_A_TEAPOT);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    @PutMapping("/me/assignjob")
    public ResponseEntity<Job> assignToMe(
            @RequestParam String jobId,
            @AuthenticationPrincipal Jwt jwt) {
        String userEmail = jwt.getClaimAsString("email");
       
        Optional<Job> job = jobService.assignJob(jobId, userEmail);
        JobState state = job.get().getJobState();
        String description = job.get().getDescription();
        String details = job.get().getDetailDescription();

        String assignSubject = "Assigned job with status " +state;
        String assignMessage = "Hi, the job " +description +" was marked as " +state +"\nThis job contains the following task: " +details;

        if (job.isPresent()) {
            mail.setSubject(assignSubject);
            mail.setMessage(assignMessage);
            mail.setTo(userEmail);

            if (mailService.sendMail(mail)) {
                return new ResponseEntity<>(job.get(), HttpStatus.OK);
            }
            return new ResponseEntity<>(job.get(), HttpStatus.I_AM_A_TEAPOT);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/me/completejob")
    public ResponseEntity<Job> completeMyJob(
            @RequestParam String jobId,
            @AuthenticationPrincipal Jwt jwt) {
        String userEmail = jwt.getClaimAsString("email");

        Optional<Job> job = jobService.completeJob(jobId, userEmail);
        JobState state = job.get().getJobState();
        String description = job.get().getDescription();
        String details = job.get().getDetailDescription();

        String completeSubject = "Completed job with status " +state;
        String completeMessage = "Hi, the job " + description + " was marked as " + state +"\n all following tasks are finished: " +details;

        if (job.isPresent()) {
            mail.setSubject(completeSubject);
            mail.setMessage(completeMessage);
            mail.setTo(userEmail);

            if (mailService.sendMail(mail)) {
                return new ResponseEntity<>(job.get(), HttpStatus.OK);
            }
            return new ResponseEntity<>(job.get(), HttpStatus.I_AM_A_TEAPOT);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/me/freelancer")
    public ResponseEntity<Freelancer> getMyId(@AuthenticationPrincipal Jwt jwt) {
        String userEmail = jwt.getClaimAsString("email");
        Freelancer freelancer = freelancerRepository.findFirstByEmail(userEmail);
        if (freelancer != null) {
            return new ResponseEntity<Freelancer>(freelancer, HttpStatus.OK);
        } else {
            return new ResponseEntity<Freelancer>(HttpStatus.BAD_REQUEST);
        }
    }
}
