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
class LogicChatEcho_requestForwardTest {

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
    void send_delegatesToClientSend() throws Exception {
        HttpClient client = mock(HttpClient.class);
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://test")).build();
        HttpResponse<String> expected = mock(HttpResponse.class);
        when(client.send(request, HttpResponse.BodyHandlers.ofString())).thenReturn(expected);

        HttpResponse<String> actual = logicChatEcho.send(client, request);

        assertThat(actual).isSameAs(expected);
    }

    @Test
    void forwardRequest_returnsContentStructResponseOn2xx() throws Exception {
        HttpClient client = mock(HttpClient.class);
        HttpResponse<String> response = mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn("response body");
        doReturn(client).when(logicChatEcho).getHttpClientMapToSession(session);
        doReturn(response).when(logicChatEcho).send(any(HttpClient.class), any(HttpRequest.class));

        StructResponse result = logicChatEcho.forwardRequest(session, "test body");

        assertThat(result.status()).isEqualTo(HttpStatus.OK);
        assertThat(result.key()).isEqualTo("Content");
        assertThat(result.value()).isEqualTo("response body");
    }

    @Test
    void forwardRequest_returnsErrorStructResponseOnNon2xx() throws Exception {
        HttpClient client = mock(HttpClient.class);
        HttpResponse<String> response = mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(500);
        when(response.body()).thenReturn("error body");
        doReturn(client).when(logicChatEcho).getHttpClientMapToSession(session);
        doReturn(response).when(logicChatEcho).send(any(HttpClient.class), any(HttpRequest.class));

        StructResponse result = logicChatEcho.forwardRequest(session, "test body");

        assertThat(result.status()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(result.key()).isEqualTo("Error");
        assertThat(result.value()).isEqualTo("error body");
    }

    @Test
    void forwardRequest_returnsExceptionStructResponseOnIOException() throws Exception {
        IOException exception = new IOException("connection refused");
        doReturn(mock(HttpClient.class)).when(logicChatEcho).getHttpClientMapToSession(session);
        doThrow(exception).when(logicChatEcho).send(any(HttpClient.class), any(HttpRequest.class));

        StructResponse result = logicChatEcho.forwardRequest(session, "test body");

        assertThat(result.status()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(result.key()).isEqualTo("Error");
        assertThat(result.value()).isEqualTo("connection refused");
        assertThat(result.exception()).isSameAs(exception);
    }

    @Test
    void forwardRequest_buildsRequestWithCorrectUriAndPostMethod() throws Exception {
        doReturn(mock(HttpClient.class)).when(logicChatEcho).getHttpClientMapToSession(session);
        doReturn(mock(HttpResponse.class)).when(logicChatEcho).send(any(HttpClient.class), any(HttpRequest.class));

        logicChatEcho.forwardRequest(session, "data");

        ArgumentCaptor<HttpRequest> captor = ArgumentCaptor.forClass(HttpRequest.class);
        verify(logicChatEcho).send(any(HttpClient.class), captor.capture());
        HttpRequest request = captor.getValue();

        assertThat(request.uri()).isEqualTo(URI.create("http://localhost:8080"));
        assertThat(request.method()).isEqualTo("POST");
    }
}
