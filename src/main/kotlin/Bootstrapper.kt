import Config.Companion.loadConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

typealias ChannelWithPlatform = Pair<String, StreamingPlatform>

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
                    .groupBy({ it.second }, { it.first })

                channels.forEach { (platform, channels) ->
                    checkerDispatcher.dispatch(channels, platform).forEach { stream ->
                        notificationDispatcher.dispatch(
                            String.format(
                                config.notificationMessage,
                                stream.userName,
                                stream.gameName,
                                stream.title
                            )
                        )
                    }
                }
                delay(config.retryDelayInSeconds * 1000)
            }
        }
    }

    private fun readChannels(config: Config): List<ChannelWithPlatform> {
        return config.channels
            .filter { it.isNotEmpty() }
            .filter { it.contains(DELIMITER) }
            .map {
                val params = it.split(DELIMITER)
                params[0].trim() to params[1].trim()
            }.map {
                when (it.second) {
                    StreamingPlatform.TWITCH.title -> ChannelWithPlatform(it.first, StreamingPlatform.TWITCH)
                    StreamingPlatform.VK_PLAY.title -> ChannelWithPlatform(it.first, StreamingPlatform.VK_PLAY)
                    else -> throw Exception("Unknown platform ${it.second}")
                }
            }
    }
}

fun main() {
    Bootstrapper()
}