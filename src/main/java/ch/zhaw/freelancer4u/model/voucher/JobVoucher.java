package ch.zhaw.freelancer4u.model.voucher;

import org.springframework.data.annotation.Id;

import ch.zhaw.freelancer4u.model.JobState;
import ch.zhaw.freelancer4u.model.JobType;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class JobVoucher {
    @Id
    private String id;

    @NonNull
    private String description;

    @NonNull
    private Double earnings;

    @NonNull
    private JobType jobType;

    private JobState jobState = JobState.NEW;

    private String freelancerId;
}
