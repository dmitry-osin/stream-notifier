/*
 * Copyright (c) 2024 Dmitry Osin <d@osin.pro>
 */

/**
 * Data class representing the configuration for the bot.
 * 
 * This class is used to store and represent the configuration settings for the bot.
 */
data class Config(
    val twitchClientId: String,
    val twitchClientSecret: String,
    val twitchOAuthToken: String,
    val telegramToken: String,
    val telegramChatId: String,
    val retryDelayInSeconds: Long,
    val locale: String,
    val botName: String,
    val channels: List<String> = emptyList(),
    val notificationMessage: String,
    val discordToken: String,
    val discordChannelId: String
) {
    /**
     * Companion object providing methods for loading configuration from environment variables.
     */
    companion object {
        /**
         * Loads the configuration from environment variables.
         * 
         * This method reads the necessary configuration settings from environment variables
         * and returns a new instance of [Config].
         * 
         * @return A new instance of [Config] with the loaded configuration settings.
         */
        fun loadConfig(): Config {

            val twitchClientId = System.getenv("bot_twitchClientId")
            val twitchClientSecret = System.getenv("bot_twitchClientSecret")
            val telegramToken = System.getenv("bot_telegramToken")
            val telegramChatId = System.getenv("bot_telegramChatId")
            val retryDelayInSeconds = System.getenv("bot_retryDelayInSeconds").toLong()
            val locale = System.getenv("bot_locale")
            val botName = System.getenv("bot_name")
            val channels = System.getenv("bot_channels").split(",").filter { it.isNotEmpty() }
            val notificationMessage = System.getenv("bot_notificationMessage")
            val twitchOAuthToken = System.getenv("bot_twitchOAuthToken")
            val discordToken = System.getenv("bot_discordToken")
            val discordChannelId = System.getenv("bot_discordChannelId")

            return Config(
                twitchClientId = twitchClientId,
                twitchClientSecret = twitchClientSecret,
                twitchOAuthToken = twitchOAuthToken,
                telegramToken = telegramToken,
                telegramChatId = telegramChatId,
                retryDelayInSeconds = retryDelayInSeconds,
                locale = locale,
                botName = botName,
                channels = channels,
                notificationMessage = notificationMessage,
                discordToken = discordToken,
                discordChannelId = discordChannelId
            )
        }
    }
} 