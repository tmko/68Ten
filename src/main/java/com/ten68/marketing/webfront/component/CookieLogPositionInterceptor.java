package com.ten68.marketing.webfront.component;


import jakarta.servlet.http.Cookie;
import org.jspecify.annotations.Nullable;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public class CookieLogPositionInterceptor implements HandshakeInterceptor {
    public static final String LAST_LOG_TIMESTAMP_COOKIE = "LAST_LOG_TIMESTAMP_COOKIE";
    private static final int EARLIEST_TIMESTAMP = 0;

    private static final Predicate<Cookie> isLogPositionCookie =
            c -> LAST_LOG_TIMESTAMP_COOKIE.equalsIgnoreCase(c.getName());

    private int intOf (String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return EARLIEST_TIMESTAMP;
        }
    }

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes
    ) {

        //Set the attribute when it is a request
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            Cookie[] cookies = servletRequest.getServletRequest().getCookies();

            String cookieValue = Optional.ofNullable(cookies)
                    .stream()
                    .flatMap(Arrays::stream)
                    .filter(isLogPositionCookie)
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse("%d".formatted(EARLIEST_TIMESTAMP));

            attributes.put(LAST_LOG_TIMESTAMP_COOKIE, intOf(cookieValue));
        }

        return true;
    }

    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            @Nullable Exception exception
    ) {
        //Set the attribute when it is a request
        /*if (response instanceof ServletServerHttpResponse) {

            Object value = attributes.getOrDefault(LAST_LOG_TIMESTAMP_COOKIE, "%d".formatted(EARLIEST_TIMESTAMP));
            Cookie cookie = new Cookie(LAST_LOG_TIMESTAMP_COOKIE, value.toString());

            ServletServerHttpResponse servletResponse = (ServletServerHttpResponse) response;
            servletResponse.getServletResponse().
            servletResponse.getServletResponse().addCookie(cookie);
        }*/

    }


}
