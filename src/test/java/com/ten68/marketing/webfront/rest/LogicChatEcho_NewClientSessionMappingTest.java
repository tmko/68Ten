package com.ten68.marketing.webfront.rest;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogicChatEcho_NewClientSessionMappingTest {

    private HttpSession session;
    private LogicChatEcho logicChatEcho;

    @BeforeEach
    void setUp() {
        session = mock(HttpSession.class);
        logicChatEcho = spy(new LogicChatEcho());
        ReflectionTestUtils.setField(logicChatEcho, "forwardUrl", "http://localhost");
        ReflectionTestUtils.setField(logicChatEcho, "forwardPort", 8080);
        ReflectionTestUtils.setField(logicChatEcho, "timeOutInSecond", 30);
    }

    @Test
    void getHttpClientMapToSession_createsNewClientWhenSessionHasNoAttribute() {
        when(session.getAttribute("SESSION_HTTP_CLIENT")).thenReturn(null);

        HttpClient client = logicChatEcho.getHttpClientMapToSession(session);

        assertThat(client).isNotNull();
        verify(session).setAttribute(eq("SESSION_HTTP_CLIENT"), any(HttpClient.class));
    }

    @Test
    void getHttpClientMapToSession_returnsExistingClientWhenNotTerminated() {
        HttpClient existing = mock(HttpClient.class);
        when(existing.isTerminated()).thenReturn(false);
        when(session.getAttribute("SESSION_HTTP_CLIENT")).thenReturn(existing);

        HttpClient result = logicChatEcho.getHttpClientMapToSession(session);

        assertThat(result).isSameAs(existing);
        verify(session, never()).setAttribute(any(), any());
    }

    @Test
    void getHttpClientMapToSession_createsNewClientWhenExistingIsTerminated() {
        HttpClient terminated = mock(HttpClient.class);
        when(terminated.isTerminated()).thenReturn(true);
        when(session.getAttribute("SESSION_HTTP_CLIENT")).thenReturn(terminated);

        HttpClient result = logicChatEcho.getHttpClientMapToSession(session);

        assertThat(result).isNotNull().isNotSameAs(terminated);
        verify(session).setAttribute(eq("SESSION_HTTP_CLIENT"), any(HttpClient.class));
    }

}
