import Config.Companion.loadConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class Notification(
    val channel: String,
    val streamingPlatform: StreamingPlatform,
    val notificationPlatform: NotificationPlatform
) {
    companion object {

        fun from(channel: String): Notification {
            val args = channel.split(DELIMITER)
            return Notification(
                args[0].trim(),
                toStreamingPlatform(args[1].trim()),
                toNotificationPlatform(args[2].trim())
            )
        }

        private fun toStreamingPlatform(platform: String): StreamingPlatform {
            return when (platform) {
                StreamingPlatform.TWITCH.title -> StreamingPlatform.TWITCH
                StreamingPlatform.VK_PLAY.title -> StreamingPlatform.VK_PLAY
                else -> throw Exception("Unknown platform $platform")
            }
        }

        private fun toNotificationPlatform(platform: String): NotificationPlatform {
            return when (platform) {
                NotificationPlatform.TELEGRAM.title -> NotificationPlatform.TELEGRAM
                NotificationPlatform.DISCORD.title -> NotificationPlatform.DISCORD
                else -> throw Exception("Unknown platform $platform")
            }
        }
    }
}

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