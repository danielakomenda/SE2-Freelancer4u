package ch.zhaw.freelancer4u.model.chatGPT;

import java.util.ArrayList;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Messages {
    private final String model = "gpt-3.5-turbo";
    private ArrayList<Message> messages;   
}
