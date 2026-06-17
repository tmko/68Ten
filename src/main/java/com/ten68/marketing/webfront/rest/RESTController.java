package com.ten68.marketing.webfront.rest;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/v1/api")
public class RESTController {
    private static final String SESSION_HTTP_CLIENT = "SESSION_HTTP_CLIENT";

    @Value("${spring.forward.url}")
    private String forwardUrl;

    @Value("${spring.forward.port}")
    private int forwardPort;


    private HttpClient getHttpClientFromSession (HttpSession session) {
        HttpClient httpClient = (HttpClient)session.getAttribute(SESSION_HTTP_CLIENT);
        boolean newClientNeeded = httpClient == null || httpClient.isTerminated();

        if (newClientNeeded) {
            httpClient = HttpClient.newHttpClient();
            session.setAttribute(SESSION_HTTP_CLIENT, httpClient);
        }
        return httpClient;
    }

    private HttpResponse<String> forwardHttpRequest (HttpClient httpClient, URI uri, String body) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    // 2. POST request endpoint
    @PostMapping("/chat")
    public ResponseEntity<String> forwardRequest(HttpSession session, @RequestBody String body) {
        final URI uri = URI.create("%s:%s".formatted(forwardUrl, forwardPort));
        try {

            HttpResponse<String> response = forwardHttpRequest(
                    getHttpClientFromSession(session),
                    uri,
                    body
            );
            return ResponseEntity.status(response.statusCode()).body(response.body());

        } catch (Exception e) {
            return respond(HttpStatus.INTERNAL_SERVER_ERROR, "Error", e.getMessage());
        }
    }

    @PostMapping("/echo")
    public ResponseEntity<String> echoRequest(@RequestBody String input) {
        return respond(HttpStatus.OK, "Echo", input);
    }


    private ResponseEntity<String> respond (HttpStatus status, String key, String value) {
        String encodedValue = URLEncoder.encode(value, StandardCharsets.UTF_8);
        String simpleSingleJSON = "{'%s':'%s'}".formatted(key,encodedValue);
        return ResponseEntity
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(simpleSingleJSON);
    }


}
