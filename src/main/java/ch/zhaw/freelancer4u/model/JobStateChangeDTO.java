package ch.zhaw.freelancer4u.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class JobStateChangeDTO {

    private String jobId;
    private String freelancerEmail;

}