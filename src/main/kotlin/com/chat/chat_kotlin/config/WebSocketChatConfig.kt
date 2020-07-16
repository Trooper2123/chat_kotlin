package com.chat.chat_kotlin.config


import com.rabbitmq.client.AMQP
import com.rabbitmq.client.Channel
import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.connection.Connection
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.boot.autoconfigure.amqp.RabbitProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer


@Configuration
@EnableRabbit
@EnableWebSocketMessageBroker
class WebSocketChatConfig : WebSocketMessageBrokerConfigurer {


    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/websocket").withSockJS()
    }

    //    localhost enable
    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.setApplicationDestinationPrefixes("/app")
        registry.enableStompBrokerRelay("/topic")
//                .setRelayHost("127.0.0.1")
//                .setRelayHost("localhost")
//                .setRelayPort(61613)
//                .setClientLogin("guest")
//                .setClientPasscode("guest")

    }

    @Bean
    fun rabbitConnectionFactory(config: RabbitProperties?): CachingConnectionFactory? {
        val connectionFactory = CachingConnectionFactory()
        connectionFactory.rabbitConnectionFactory.setUri("amqp://pzfmgmdb:48oHHAJQFmDgsSXXY9SG3evjwgkDCqPG@clam.rmq.cloudamqp.com/pzfmgmdb")
        val conn: Connection = connectionFactory.createConnection()
        val channel: Channel = conn.createChannel(true)
        val admin = RabbitAdmin(connectionFactory)
        admin.declareQueue()
        return connectionFactory
    }

    @Bean
    fun myQueue(): AMQP.Queue? {
        return AMQP.Queue()
    }

}
