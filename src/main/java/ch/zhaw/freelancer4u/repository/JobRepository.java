package ch.zhaw.freelancer4u.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import ch.zhaw.freelancer4u.model.Job;


public interface JobRepository extends MongoRepository<Job, String> {
    
}
