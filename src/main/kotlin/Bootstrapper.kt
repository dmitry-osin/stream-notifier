/*
 * Copyright (c) 2024 Dmitry Osin <d@osin.pro>
 */

import Config.Companion.loadConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Data class representing a notification configuration.
 * 
 * This class is used to store and represent the configuration settings for a notification.
 */
data class Notification(
    val channel: String,
    val streamingPlatform: StreamingPlatform,
    val notificationPlatform: NotificationPlatform
) {
    companion object {

        /**
         * Creates a [Notification] object from a channel string.
         * 
         * This method parses the channel string and extracts the necessary configuration settings.
         * 
         * @param channel The channel string containing the notification configuration.
         * @return A new instance of [Notification] with the parsed configuration settings.
         */
        fun from(channel: String): Notification {
            val args = channel.split(DELIMITER)
            return Notification(
                args[0].trim(),
                toStreamingPlatform(args[1].trim()),
                toNotificationPlatform(args[2].trim())
            )
        }

        /**
         * Converts a platform string to a [StreamingPlatform] enum.
         * 
         * This method maps the platform string to the corresponding enum value.
         * 
         * @param platform The platform string to convert.
         * @return The corresponding [StreamingPlatform] enum value.
         * @throws Exception if the platform string is not recognized.
         */
        private fun toStreamingPlatform(platform: String): StreamingPlatform {
            return when (platform) {
                StreamingPlatform.TWITCH.title -> StreamingPlatform.TWITCH
                StreamingPlatform.VK_PLAY.title -> StreamingPlatform.VK_PLAY
                else -> throw Exception("Unknown platform $platform")
            }
        }

        /**
         * Converts a platform string to a [NotificationPlatform] enum.
         * 
         * This method maps the platform string to the corresponding enum value.
         * 
         * @param platform The platform string to convert.
         * @return The corresponding [NotificationPlatform] enum value.
         * @throws Exception if the platform string is not recognized.
         */
        private fun toNotificationPlatform(platform: String): NotificationPlatform {
            return when (platform) {
                NotificationPlatform.TELEGRAM.title -> NotificationPlatform.TELEGRAM
                NotificationPlatform.DISCORD.title -> NotificationPlatform.DISCORD
                else -> throw Exception("Unknown platform $platform")
            }
        }
    }
}

/**
 * Main class for starting the bot.
 * 
 * This class is responsible for initializing and starting the bot.
 */
class Bootstrapper {
    private val config = loadConfig()
    private val notificationDispatcher: NotificationStrategyDispatcher = NotificationStrategyDispatcher(config)
    private val checkerDispatcher: CheckerStrategyDispatcher = CheckerStrategyDispatcher(config)

    init {
        startBot()
    }

    private fun startBot() {
        startStreamChecking()
    }

    /**
     * Starts the stream checking process.
     * 
     * This method initializes and starts the stream checking process using coroutines.
     * It continuously checks for active streams and sends notifications when streams are found.
     */
    private fun startStreamChecking() {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {

                val channels = readChannels(config)

                channels.forEach { notification ->
                    checkerDispatcher.dispatch(notification).forEach { stream ->
                        notificationDispatcher.dispatch(
                            String.format(
                                config.notificationMessage,
                                stream.userName,
                                stream.gameName,
                                stream.title
                            ),
                            notification.notificationPlatform
                        )
                    }
                }
                delay(config.retryDelayInSeconds * 1000)
            }
        }
    }

    private fun readChannels(config: Config): List<Notification> {
        return config.channels
            .filter { it.isNotEmpty() }
            .filter { it.contains(DELIMITER) }
            .map {
                Notification.from(it)
            }
    }
}

fun main() {
    Bootstrapper()
}