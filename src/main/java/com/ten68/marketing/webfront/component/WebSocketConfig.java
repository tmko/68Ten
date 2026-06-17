package com.ten68.marketing.webfront.component;


import jakarta.servlet.http.Cookie;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private static final String SOCKET_URI = "/v1/api/audit";

    private final FileWebSocketHandler fileWebSocketHandler;

    public WebSocketConfig(FileWebSocketHandler fileWebSocketHandler) {
        this.fileWebSocketHandler = fileWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(fileWebSocketHandler, SOCKET_URI)
                .addInterceptors(new CookieLogPositionInterceptor());
    }
}
