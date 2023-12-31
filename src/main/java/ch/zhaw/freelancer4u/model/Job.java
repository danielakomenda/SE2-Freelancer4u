package ch.zhaw.freelancer4u.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Document("Job")
public class Job {
    @Id
    private String id;

    @NonNull
    private String description;

    @NonNull
    private String detailDescription;

    @NonNull
    private Double earnings;

    @NonNull
    private JobType jobType;

    private JobState jobState = JobState.NEW;

    private String freelancerId;


}
