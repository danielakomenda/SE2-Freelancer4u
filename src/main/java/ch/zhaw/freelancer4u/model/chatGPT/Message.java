package ch.zhaw.freelancer4u.model.chatGPT;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Message {
 
    private String role;
    private String content;
}
