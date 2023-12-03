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


    // Method for logged in User to assign to a new job
    public Optional<Job> assignJob(String jobId, String freelancerEmail) {
        Optional<Job> jobToAssign = jobRepository.findById(jobId);
        Freelancer freelancer = freelancerRepository.findFirstByEmail(freelancerEmail);

        if (jobToAssign.isPresent()) {
            Job job = jobToAssign.get();
            if (job.getJobState() == JobState.NEW) {
                if (freelancer!=null){
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


    // Method for logged in User to complete his job
    public Optional<Job> completeJob(String jobId, String freelancerEmail){
        Optional<Job> jobToComplete = jobRepository.findById(jobId);

        if (jobToComplete.isPresent() && jobToComplete.get().getJobState() == JobState.ASSIGNED) {            
            Optional<Freelancer> freelancer = freelancerRepository.findById(jobToComplete.get().getFreelancerId());
            
            Job job = jobToComplete.get();
            if (freelancer.isPresent() && freelancer.get().getEmail().equals(freelancerEmail)){
                job.setJobState(JobState.DONE);
                jobRepository.save(job);
                return Optional.of(job);
            }
        }
        return Optional.empty();

    }
}