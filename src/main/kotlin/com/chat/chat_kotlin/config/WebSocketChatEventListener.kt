package com.chat.chat_kotlin.config

import com.chat.chat_kotlin.domain.WebSocketChatMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionConnectedEvent
import org.springframework.web.socket.messaging.SessionDisconnectEvent

@Component
class WebSocketChatEventListener {
    @Autowired
    private val messagingTemplate: SimpMessageSendingOperations? = null

    @EventListener
    fun handleWebSocketConnectListener(event: SessionConnectedEvent?) {
        println("Received a new web socket connection")
    }

    @EventListener
    fun handleWebSocketDisconnectListener(event: SessionDisconnectEvent) {
        val headerAccessor = StompHeaderAccessor.wrap(event.message)
        val username = headerAccessor.sessionAttributes!!["username"] as String?
        if (username != null) {
            val chatMessage = WebSocketChatMessage()
            chatMessage.type = "Leave"
            chatMessage.sender = username
            messagingTemplate!!.convertAndSend("/topic/websocket", chatMessage)
        }
        print("Xau!")
    }
}
