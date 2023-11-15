package ch.zhaw.freelancer4u.repository;


import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import ch.zhaw.freelancer4u.model.Job;
import ch.zhaw.freelancer4u.model.JobType;


public interface JobRepository extends MongoRepository<Job, String> {
    List<Job> findByEarningsGreaterThan(Double earnings);

    List<Job> findByJobType(JobType type);
}
