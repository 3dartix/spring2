package ru.geekbrains.config;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import ru.geekbrains.chat.ChatPrincipal;

import java.security.Principal;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
//@EnableWebSocket для обмена байтами
@EnableWebSocketMessageBroker //отправка текстовых сообщений
@CommonsLog
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final AtomicInteger userCounter = new AtomicInteger(1);

    //создаем для каждого пользователя
    @Bean
    public DefaultHandshakeHandler customHandshakeHandler() {
        return new DefaultHandshakeHandler() {

            @Override
            protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
                ChatPrincipal principal = new ChatPrincipal("user_" + userCounter.getAndIncrement());
                log.info("New WebSocket chat user " + principal.getName());
                return principal;
            }
        };
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // выделяем из всего потока сообщений сообщения с префиксами, которые должны обрабатываться
        // отправка сообщений
        registry.setApplicationDestinationPrefixes("/chat_in");
        registry.enableSimpleBroker("/chat_out"); //
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/gs-guide-websocket") // url для превода в ржим вебсокетов
                .setHandshakeHandler(customHandshakeHandler())
                .withSockJS()
                .setClientLibraryUrl("/webjars/sockjs-client/1.0.2/sockjs.min.js"); //исходники библиотеки
    }
}
