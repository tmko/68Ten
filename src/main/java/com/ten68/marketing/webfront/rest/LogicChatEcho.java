package com.ten68.marketing.webfront.rest;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class LogicChatEcho {
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


    public StructResponse forwardRequest(HttpSession session, @RequestBody String body) throws IOException, InterruptedException {
        final URI uri = URI.create("%s:%s".formatted(forwardUrl, forwardPort));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpClient client = getHttpClientFromSession(session);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return
            HttpStatus.OK.value() != response.statusCode() ?
            new StructResponse(HttpStatus.OK, "Content", response.body()) :
            new StructResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error", response.body());
    }



}
