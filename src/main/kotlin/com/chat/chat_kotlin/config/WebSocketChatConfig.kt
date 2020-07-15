package com.chat.chat_kotlin.config




import org.springframework.amqp.core.AmqpAdmin
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.boot.autoconfigure.amqp.RabbitProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer


@Configuration
@EnableWebSocketMessageBroker
class WebSocketChatConfig : WebSocketMessageBrokerConfigurer {


    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/websocket").withSockJS()
    }

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.setApplicationDestinationPrefixes("/app")
        registry.enableStompBrokerRelay("/topic")
//                .setRelayHost("clam.rmq.cloudamqp.com")
//                .setRelayPort(5672)
//                .setClientLogin("pzfmgmdb")
//                .setClientPasscode("48oHHAJQFmDgsSXXY9SG3evjwgkDCqPGD")
    }

    @Bean
    fun rabbitConnectionFactory(config: RabbitProperties?): CachingConnectionFactory? {
        val connectionFactory = CachingConnectionFactory()
        connectionFactory.rabbitConnectionFactory.setUri("amqp://pzfmgmdb:48oHHAJQFmDgsSXXY9SG3evjwgkDCqPG@clam.rmq.cloudamqp.com/pzfmgmdb")
        val admin = RabbitAdmin(connectionFactory)
        admin.declareQueue()
        return connectionFactory
    }

    @Bean
    fun connectionFactory(): CachingConnectionFactory {
        return CachingConnectionFactory("pzfmgmdb")
    }

    @Bean
    fun amqpAdmin(): AmqpAdmin? {
        return connectionFactory()?.let { RabbitAdmin(it) }
    }

    @Bean
    fun rabbitTemplate(): RabbitTemplate? {
        return connectionFactory()?.let { RabbitTemplate(it) }
    }

    @Bean
    fun myQueue(): Queue? {
        return Queue("myqueue")
    }

}