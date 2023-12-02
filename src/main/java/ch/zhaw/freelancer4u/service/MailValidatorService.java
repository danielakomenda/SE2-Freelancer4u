package ch.zhaw.freelancer4u.service;

import java.time.Duration;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ch.zhaw.freelancer4u.model.MailInformation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Service
public class MailValidatorService {
    private static final String DISIFY_EMAIL_VALIDATOR_BASE_URL = "https://www.disify.com";
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(3);
    private static final String USER_AGENT = "Spring 5 WebClient";
    private static final Logger logger = LoggerFactory.getLogger(MailValidatorService.class);
    private final WebClient webClient;

    @Autowired
    public MailValidatorService() {
        this.webClient = WebClient.builder()
                .baseUrl(DISIFY_EMAIL_VALIDATOR_BASE_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.USER_AGENT, USER_AGENT)
                .filter(ServiceUtils.logRequest(logger))
                .build();
    }

    public MailInformation validateEmail(String email) {
        return webClient
            .get()
            .uri("/api/email/" + email)
            .retrieve().bodyToMono(MailInformation.class)
            .block(REQUEST_TIMEOUT);
    }
}
