package com.chat.chat_kotlin

import org.json.JSONObject
import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Component
class SocketTextHandler: TextWebSocketHandler() {

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val payload: String = message.payload
        val jsonObject = JSONObject(payload)

        session.sendMessage(TextMessage("Hi "+ jsonObject.get("user") + " how may I help you?"))
    }
}