/*
 * Copyright (c) 2024 Dmitry Osin <d@osin.pro>
 */

import net.dv8tion.jda.api.JDABuilder
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession

/**
 * Interface defining notification functionality.
 * Implementations of this interface are responsible for sending notifications
 * to different platforms (e.g., Telegram, Discord).
 */
sealed interface INotifier {

    /**
     * Sends a notification message to the specified platform.
     *
     * @param message The message to send
     */
    fun sendNotification(message: String)

}

enum class NotificationPlatform(val title: String) {
    TELEGRAM("telegram"),
    DISCORD("discord");
}

/**
 * Implementation of [INotifier] for Telegram platform.
 * 
 * This class is responsible for sending notifications to a Telegram chat.
 */
class TelegramNotifierBot(
    token: String,
    private val chatId: String
) : INotifier, TelegramLongPollingBot(token) {

    init {
        TelegramBotsApi(DefaultBotSession::class.java).registerBot(this)
    }

    override fun getBotUsername(): String = "TwitchNotifierBot"

    override fun onUpdateReceived(update: Update) {

    }

    override fun sendNotification(message: String) {
        val sendMessage = SendMessage()
        sendMessage.chatId = chatId
        sendMessage.text = message
        execute(sendMessage)
    }
}

/**
 * Implementation of [INotifier] for Discord platform.
 * 
 * This class is responsible for sending notifications to a Discord channel.
 */
class DiscordNotifierBot(
    token: String,
    private val channelId: String
) : INotifier {
    private val client = JDABuilder.createDefault(token)
        .build()
        .awaitReady()

    override fun sendNotification(message: String) {
        val channel = client.getTextChannelById(channelId) ?: return
        channel.sendMessage(message).queue()
    }
}