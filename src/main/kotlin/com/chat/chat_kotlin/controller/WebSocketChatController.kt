package com.chat.chat_kotlin.controller

import com.chat.chat_kotlin.domain.WebSocketChatMessage
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.stereotype.Controller

@Controller
class WebSocketChatController {
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/websocket")
    fun sendMessage (@Payload webSocketChatMessage: WebSocketChatMessage): WebSocketChatMessage {
    return webSocketChatMessage
    }

    @MessageMapping("chat.newUser")
    @SendTo("/topic/websocket")
    fun newUser(@Payload webSocketChatMessage: WebSocketChatMessage, headerAccessor: SimpMessageHeaderAccessor): WebSocketChatMessage{
        headerAccessor.sessionAttributes?.put("username",webSocketChatMessage.sender)
        return webSocketChatMessage
    }
}