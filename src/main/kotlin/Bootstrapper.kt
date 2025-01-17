import Config.Companion.loadConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

typealias ChannelWithPlatform = Pair<String, StreamingPlatform>

class Bootstrapper {
    private val config = loadConfig()
    private val notificationDispatcher: NotificationStrategyDispatcher = NotificationStrategyDispatcher(config)
    private val checkerDispatcher: CheckerStrategyDispatcher = CheckerStrategyDispatcher(config)
    private val locale = Locale(config.locale)
    private val bundle = ResourceBundle.getBundle(MESSAGES_FILE, locale)

    init {
        startBot()
    }

    private fun startBot() {
        startStreamChecking()
    }

    private fun startStreamChecking() {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {

                val channels = readChannels()
                    .groupBy({ it.second }, { it.first })

                channels.forEach { (platform, channels) ->
                    checkerDispatcher.dispatch(channels, platform).forEach { stream ->
                        notificationDispatcher.dispatch(
                            String.format(
                                bundle.getString("stream.started"),
                                stream.title,
                                stream.userName,
                                stream.gameName
                            )
                        )
                    }
                }
                delay(config.notificationDelayInSeconds * 1000)
            }
        }
    }

    private fun readChannels(path: String = CHANNELS_FILE): List<ChannelWithPlatform> {
        return File(path).readLines()
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