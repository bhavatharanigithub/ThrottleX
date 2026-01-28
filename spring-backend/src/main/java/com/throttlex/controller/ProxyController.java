package com.throttlex.controller;

import com.throttlex.service.RateLimiterService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProxyController {

    private final WebClient webClient;
    private final RateLimiterService rateLimiterService;

    public ProxyController(RateLimiterService rateLimiterService) {
        this.webClient = WebClient.builder().build();
        this.rateLimiterService = rateLimiterService;
    }

    @RequestMapping("/proxy/**")
    public Mono<ResponseEntity<String>> proxyRequest(
            HttpServletRequest request,
            @RequestHeader HttpHeaders headers,
            @RequestBody(required = false) String body
    ) {
        String targetUrl = headers.getFirst("X-Target-Url");
        if (targetUrl == null || targetUrl.isBlank()) {
            return Mono.just(ResponseEntity.badRequest().body("Missing X-Target-Url header"));
        }

        String rateKey = request.getRemoteAddr();
        if (!rateLimiterService.isAllowed(rateKey)) {
            return Mono.just(ResponseEntity.status(429).body("Rate limit exceeded"));
        }

        boolean isGetOrHead = request.getMethod().equalsIgnoreCase("GET")
                || request.getMethod().equalsIgnoreCase("HEAD");

        if (isGetOrHead) {
            WebClient.RequestHeadersSpec<?> spec = webClient
                    .method(org.springframework.http.HttpMethod.valueOf(request.getMethod()))
                    .uri(URI.create(targetUrl))
                    .headers(h -> {
                        h.addAll(headers);
                        h.remove("Host");
                        h.remove("X-Target-Url");
                    });

            return spec.exchangeToMono(clientResponse ->
                    clientResponse.bodyToMono(String.class)
                            .defaultIfEmpty("")
                            .map(responseBody -> {
                                HttpHeaders responseHeaders = new HttpHeaders();
                                clientResponse.headers().asHttpHeaders()
                                        .forEach((k, v) -> responseHeaders.put(k, List.copyOf(v)));
                                return ResponseEntity.status(clientResponse.statusCode())
                                        .headers(responseHeaders)
                                        .body(responseBody);
                            })
            ).onErrorResume(WebClientRequestException.class, ex ->
                    Mono.just(ResponseEntity.status(502).body("Upstream request failed: " + ex.getMessage()))
            );
        } else {
            WebClient.RequestBodySpec bodySpec = webClient
                    .method(org.springframework.http.HttpMethod.valueOf(request.getMethod()))
                    .uri(URI.create(targetUrl))
                    .headers(h -> {
                        h.addAll(headers);
                        h.remove("Host");
                        h.remove("X-Target-Url");
                    });

            WebClient.RequestHeadersSpec<?> specToExecute = bodySpec;

            if (body != null && !body.isEmpty()) {
                specToExecute = bodySpec.contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(body);
            }

            return specToExecute.exchangeToMono(clientResponse ->
                clientResponse.bodyToMono(String.class)
                        .defaultIfEmpty("")
                        .map(responseBody -> {
                            HttpHeaders responseHeaders = new HttpHeaders();
                            clientResponse.headers().asHttpHeaders()
                                    .forEach((k, v) -> responseHeaders.put(k, List.copyOf(v)));
                            return ResponseEntity.status(clientResponse.statusCode())
                                    .headers(responseHeaders)
                                    .body(responseBody);
                        })
            ).onErrorResume(WebClientRequestException.class, ex ->
                    Mono.just(ResponseEntity.status(502).body("Upstream request failed: " + ex.getMessage()))
            );
        }
    }
}

