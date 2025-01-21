/*
 * Copyright (c) 2024 Dmitry Osin <d@osin.pro>
 */

/**
 * Interface defining notification strategy functionality.
 * Implementations of this interface are responsible for sending notifications
 * to different platforms (e.g., Telegram, Discord).
 */
sealed interface INotificationStrategy {

    /**
     * Sends a notification message to the specified platform.
     *
     * @param message The message to send
     */
    fun sendNotification(message: String)

    /**
     * Checks if the strategy is applicable to a given platform.
     *
     * @param platform The platform to check against
     * @return True if the strategy is applicable to the platform, false otherwise
     */
    fun isApplicable(platform: NotificationPlatform): Boolean
}

/**
 * Implementation of [INotificationStrategy] for Telegram platform.
 * 
 * This class is responsible for sending notifications to a Telegram chat.
 */
class TelegramNotificationStrategy(config: Config) : INotificationStrategy {

    private val notifier = TelegramNotifierBot(config.telegramToken, config.telegramChatId)

    override fun sendNotification(message: String) {
        notifier.sendNotification(message)
    }

    override fun isApplicable(platform: NotificationPlatform): Boolean = platform == NotificationPlatform.TELEGRAM

}

/**
 * Implementation of [INotificationStrategy] for Discord platform.
 * 
 * This class is responsible for sending notifications to a Discord channel.
 */
class DiscordNotificationStrategy(config: Config) : INotificationStrategy {
    private val notifier = DiscordNotifierBot(config.discordToken, config.discordChannelId)

    override fun sendNotification(message: String) {
        notifier.sendNotification(message)
    }

    override fun isApplicable(platform: NotificationPlatform): Boolean = platform == NotificationPlatform.DISCORD
}

/**
 * Dispatcher class for handling notification strategies.
 * 
 * This class is responsible for dispatching notifications to the appropriate strategy
 * based on the platform specified in the notification configuration.
 */
class NotificationStrategyDispatcher(config: Config) {

    private val telegramBot = TelegramNotificationStrategy(config)
    private val discordBot = DiscordNotificationStrategy(config)
    private val strategies = setOf(telegramBot, discordBot)

    fun dispatch(message: String, platform: NotificationPlatform) {
        for (strategy in strategies) {
            if (strategy.isApplicable(platform)) {
                strategy.sendNotification(message)
            }
        }
    }

}