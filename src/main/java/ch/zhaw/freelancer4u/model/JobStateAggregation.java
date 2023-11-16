package ch.zhaw.freelancer4u.model;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class JobStateAggregation {
    private String id;
    private List<String> jobIds;
    private String count;
}
