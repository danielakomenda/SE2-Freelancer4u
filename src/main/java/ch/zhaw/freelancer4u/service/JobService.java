package ch.zhaw.freelancer4u.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.zhaw.freelancer4u.model.Freelancer;
import ch.zhaw.freelancer4u.model.Job;
import ch.zhaw.freelancer4u.model.JobState;
import ch.zhaw.freelancer4u.repository.FreelancerRepository;
import ch.zhaw.freelancer4u.repository.JobRepository;

@Service
public class JobService {
    @Autowired
    JobRepository jobRepository;

    @Autowired
    FreelancerRepository freelancerRepository;

    public Optional<Job> assignJob(String jobId, String freelancerEmail) {
        Optional<Job> jobToAssign = jobRepository.findById(jobId);
        Freelancer freelancer = freelancerRepository.findFirstByEmail(freelancerEmail);

        if (jobToAssign.isPresent()) {
            Job job = jobToAssign.get();
            if (job.getJobState() == JobState.NEW) {
                if (!freelancer.equals(null)){
                    String id = freelancer.getId();
                    job.setJobState(JobState.ASSIGNED);
                    job.setFreelancerId(id);
                    jobRepository.save(job);
                    return Optional.of(job);
                }
            }
        }
        return Optional.empty();
    }
}