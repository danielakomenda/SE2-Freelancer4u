package ch.zhaw.freelancer4u.model.chatGPT;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ch.zhaw.freelancer4u.model.Credentials;
import ch.zhaw.freelancer4u.service.ServiceUtils;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Service
public class ChatGPTService {

    Credentials credentials = new Credentials();

    private String CHAT_GPT_API_KEY = credentials.getOpenAIKey();
    private static final String CHAT_GPT_BASE_URL = "https://api.openai.com";
    
    // Timeout was set to 10 seconds, because chat-gpt tends to take a few seconds to respond.
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(15);
    private static final String USER_AGENT = "Spring 5 WebClient";
    private static final Logger logger = LoggerFactory.getLogger(ChatGPTService.class);
    
    private final WebClient webClient;

    @Autowired
    public ChatGPTService() {
        this.webClient = WebClient.builder()
        .baseUrl(CHAT_GPT_BASE_URL)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .defaultHeader(HttpHeaders.USER_AGENT, USER_AGENT)
        .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + CHAT_GPT_API_KEY)
        .filter(ServiceUtils.logRequest(logger))
        .build();
    }

    public MessageResponse chatWithChatGPT(String messageContent) {
        Messages messages = new Messages();
        Message message = new Message();
        message.setRole("user");
        message.setContent(messageContent);
        messages.setMessages((new ArrayList<Message>(List.of(message))));
        Gson gson = new GsonBuilder().create();
        
        logger.info(gson.toJson(messages));

        var messageRespons = webClient
        .post()
        .uri("/v1/chat/completions")
        .body(Mono.just(messages), Messages.class)
        .retrieve()
        .bodyToMono(MessageResponse.class)
        .block(REQUEST_TIMEOUT);

        return messageRespons;
    }
}
