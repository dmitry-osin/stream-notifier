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
    val notificationMessage: String
) {
    companion object {
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
                notificationMessage = notificationMessage
            )
        }
    }
} 