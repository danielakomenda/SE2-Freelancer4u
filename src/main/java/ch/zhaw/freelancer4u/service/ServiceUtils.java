package ch.zhaw.freelancer4u.service;

import org.slf4j.Logger;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

public class ServiceUtils {
    public static ExchangeFilterFunction logRequest(Logger logger) {

        return (clientRequest, next) -> {
            logger.info("Request: {} {}", clientRequest.method(),
                    clientRequest.url());
            clientRequest.headers()
                    .forEach((name, values) -> values.forEach(value -> logger.info("{}={}", name, value)));
            return next.exchange(clientRequest);
        };
    }
}
