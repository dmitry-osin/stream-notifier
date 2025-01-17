import com.github.twitch4j.TwitchClientBuilder

sealed interface ICheckerStrategy {

    fun check(channels: List<String>): List<StreamObject>

    fun isApplicable(platform: StreamingPlatform): Boolean
}

enum class StreamingPlatform(val title: String) {
    TWITCH("twitch"),
    VK_PLAY("vk");
}

class TwitchCheckerStrategy(config: Config) : ICheckerStrategy {

    private val client = TwitchClientBuilder.builder()
        .withClientId(config.twitchClientId)
        .withClientSecret(config.twitchClientSecret)
        .withEnableHelix(true)
        .build()

    private val checker = TwitchStreamChecker(client)

    override fun check(channels: List<String>): List<StreamObject> {
        return checker.checkStreams(channels)
    }

    override fun isApplicable(platform: StreamingPlatform): Boolean {
        return platform == StreamingPlatform.TWITCH
    }

}

class CheckerStrategyDispatcher(config: Config = Config.loadConfig()) {

    private val twitch = TwitchCheckerStrategy(config)
    private val strategies = setOf(twitch)

    fun dispatch(channels: List<String>, platform: StreamingPlatform): List<StreamObject> {
        val result = mutableListOf<StreamObject>()
        for (strategy in strategies) {
            if (strategy.isApplicable(platform)) {
                result += strategy.check(channels)
            }
        }
        return result
    }

}