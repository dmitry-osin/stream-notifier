sealed interface INotificationStrategy {

    fun sendNotification(message: String)

    val isApplicable: Boolean
}

class TelegramNotificationStrategy(config: Config) : INotificationStrategy {

    private val notifier = TelegramNotifierBot(config.telegramToken, config.telegramChatId)

    override fun sendNotification(message: String) {
        notifier.sendNotification(message)
    }

    override val isApplicable: Boolean
        get() = true

}

class NotificationStrategyDispatcher(config: Config) {

    private val telegramBot = TelegramNotificationStrategy(config)
    private val strategies = setOf(telegramBot)

    fun dispatch(message: String) {
        for (strategy in strategies) {
            if (strategy.isApplicable) {
                strategy.sendNotification(message)
            }
        }
    }

}