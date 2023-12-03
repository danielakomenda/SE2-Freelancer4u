package ch.zhaw.freelancer4u.model.chatGPT;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Usage {
    private int prompt_tokens;
    private int completion_tokens;
    private int total_tokens;
}
