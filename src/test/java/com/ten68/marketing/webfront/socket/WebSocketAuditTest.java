package com.ten68.marketing.webfront.socket;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.StompWebSocketEndpointRegistration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebSocketAuditTest {

    @Mock
    private LogicTailAuditLog logicTailAuditLog;

    @InjectMocks
    private WebSocketAudit webSocketAudit;

    @Test
    void process_delegatesToLogicTailAuditLog() throws Exception {
        StructMessage input = new StructMessage(100L, "input");
        List<StructMessage> expected = List.of(new StructMessage(200L, "output"));
        when(logicTailAuditLog.process(input)).thenReturn(expected);

        List<StructMessage> result = webSocketAudit.process(input);

        assertThat(result).isSameAs(expected);
        verify(logicTailAuditLog).process(input);
    }

    @Test
    void process_returnsWhatLogicTailAuditLogReturns() throws Exception {
        StructMessage input = new StructMessage(100L, "input");
        when(logicTailAuditLog.process(input)).thenReturn(List.of());

        List<StructMessage> result = webSocketAudit.process(input);

        assertThat(result).isEmpty();
    }

    @Test
    void configureMessageBroker_enablesSimpleBroker() {
        MessageBrokerRegistry registry = mock(MessageBrokerRegistry.class);

        webSocketAudit.configureMessageBroker(registry);

        verify(registry).enableSimpleBroker("/topic");
        verify(registry).setApplicationDestinationPrefixes("/v1/api");
    }

    @Test
    void registerStompEndpoints_addsWebSocketEndpoint() {
        StompEndpointRegistry registry = mock(StompEndpointRegistry.class);
        StompWebSocketEndpointRegistration registration = mock(StompWebSocketEndpointRegistration.class);
        when(registry.addEndpoint("/v1/websocket")).thenReturn(registration);

        webSocketAudit.registerStompEndpoints(registry);

        verify(registry).addEndpoint("/v1/websocket");
    }

    @Test
    void constants_areCorrect() {
        assertThat(WebSocketAudit.SOCKET_SUB_TOPIC).isEqualTo("/topic/auditEvents");
        assertThat(WebSocketAudit.SOCKET_PUB_ENDPOINT).isEqualTo("audit");
    }
}
