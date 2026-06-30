package com.ten68.marketing.webfront.rest;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.net.CookieManager;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Component
public class LogicChatEcho {
    private static final String SESSION_HTTP_CLIENT = "SESSION_HTTP_CLIENT";

    @Value("${spring.forward.url}")
    private String forwardUrl;

    @Value("${spring.forward.port}")
    private int forwardPort;

    @Value("${spring.forward.TimeOutInSecond}")
    private int timeOutInSecond;

    //VisiableForTest
    HttpClient getHttpClientMapToSession(HttpSession session) {
        HttpClient httpClient = (HttpClient)session.getAttribute(SESSION_HTTP_CLIENT);

        boolean newClientNeeded = httpClient == null || httpClient.isTerminated();
        if (newClientNeeded) {
            CookieManager cookieManager = new CookieManager();

            httpClient = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(timeOutInSecond))
                    .followRedirects(HttpClient.Redirect.ALWAYS)
                    .cookieHandler(cookieManager)
                    .build();
            session.setAttribute(SESSION_HTTP_CLIENT, httpClient);
        }

        return httpClient;
    }

    HttpResponse<String> send (HttpClient client, HttpRequest request) throws IOException, InterruptedException {
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }


    public StructResponse forwardRequest(HttpSession session, @RequestBody String body) {

        HttpClient client = getHttpClientMapToSession(session);

        URI uri = URI.create("%s:%s".formatted(forwardUrl, forwardPort));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();


        try {
            HttpResponse<String> response = send(client, request);

            return HttpStatus.valueOf(response.statusCode()).is2xxSuccessful() ?
                            new StructResponse(HttpStatus.OK, "Content", response.body()) :
                            new StructResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error", response.body());

        } catch (Exception e) {
            return new StructResponse(e);
        }
    }


}
