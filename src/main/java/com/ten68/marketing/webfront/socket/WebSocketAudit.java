package com.ten68.marketing.webfront.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

@Configuration
@Controller
@EnableWebSocketMessageBroker
public class WebSocketAudit implements WebSocketMessageBrokerConfigurer {
    private static final String SOCKET_SUB = "/topic";
    private static final String SOCKET_PUB = "/v1/api";
    private static final String SOCKET_URI = "/v1/websocket";

    public  static final String SOCKET_SUB_TOPIC = "/topic/auditEvents";
    public  static final String SOCKET_PUB_ENDPOINT = "audit";

    @Autowired
    private final LogicTailAuditLog logicTailAuditLog;

    public WebSocketAudit(LogicTailAuditLog logicTailAuditLog) {
        this.logicTailAuditLog = logicTailAuditLog;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker(SOCKET_SUB);
        config.setApplicationDestinationPrefixes(SOCKET_PUB);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(SOCKET_URI);
    }

    @MessageMapping(WebSocketAudit.SOCKET_PUB_ENDPOINT)
    @SendTo(WebSocketAudit.SOCKET_SUB_TOPIC)
    public List<StructMessage> process(StructMessage message) throws Exception {
        List<StructMessage> result = logicTailAuditLog.process(message);
        return result;
    }

}

