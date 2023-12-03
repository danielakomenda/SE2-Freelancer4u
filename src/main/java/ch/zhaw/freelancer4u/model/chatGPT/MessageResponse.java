package ch.zhaw.freelancer4u.model.chatGPT;

import java.util.ArrayList;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class MessageResponse {
    private String id;
    private String object;
    private int created;
    private ArrayList<Choice> choices;
    private Usage usage;


    public String getContent() {
        return choices.get(0).getMessage().getContent();
    }
}
