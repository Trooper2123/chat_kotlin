package com.chat.chat_kotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class WebsocketChatApplication

    fun main(args: Array<String>) {
        runApplication<WebsocketChatApplication>(*args)
    }