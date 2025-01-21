sealed interface INotificationStrategy {

    fun sendNotification(message: String)

    fun isApplicable(platform: NotificationPlatform): Boolean
}

class TelegramNotificationStrategy(config: Config) : INotificationStrategy {

    private val notifier = TelegramNotifierBot(config.telegramToken, config.telegramChatId)

    override fun sendNotification(message: String) {
        notifier.sendNotification(message)
    }

    override fun isApplicable(platform: NotificationPlatform): Boolean = platform == NotificationPlatform.TELEGRAM

}

class DiscordNotificationStrategy(config: Config) : INotificationStrategy {
    private val notifier = DiscordNotifierBot(config.discordToken, config.discordChannelId)

    override fun sendNotification(message: String) {
        notifier.sendNotification(message)
    }

    override fun isApplicable(platform: NotificationPlatform): Boolean = platform == NotificationPlatform.DISCORD
}

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