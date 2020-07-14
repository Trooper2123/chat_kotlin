package com.chat.chat_kotlin.config

import com.chat.chat_kotlin.domain.WebSocketChatMessage
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionConnectEvent
import org.springframework.web.socket.messaging.SessionDisconnectEvent

@Component
class WebSocketChatEventListener(var messagingTemplate: SimpMessagingTemplate) {

    @EventListener
    fun handleWebSocketConnectListener(event: SessionConnectEvent) {
        println("Received a new web socket connection")
    }

    @EventListener
    fun handleWebSocketDisconnectListener(event: SessionDisconnectEvent) {
        val headerAccessor: StompHeaderAccessor = StompHeaderAccessor.wrap(event.message)
        val username: String = headerAccessor.sessionAttributes?.get("username").toString()
        if (username != null) {
            val chatMessage = WebSocketChatMessage()
            chatMessage.type = "Leave"
            chatMessage.sender = username
            messagingTemplate.convertAndSend("/topic/pubic", chatMessage)
        }
    }
}
