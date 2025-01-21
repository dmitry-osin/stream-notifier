import com.github.philippheuer.credentialmanager.domain.OAuth2Credential
import com.github.twitch4j.TwitchClientBuilder

sealed interface ICheckerStrategy {

    fun check(notification: Notification): List<StreamObject>

    fun isApplicable(platform: StreamingPlatform): Boolean
}

enum class StreamingPlatform(val title: String) {
    TWITCH("twitch"),
    VK_PLAY("vk");
}

class TwitchCheckerStrategy(config: Config) : ICheckerStrategy {

    private val client = TwitchClientBuilder.builder()
        .withDefaultAuthToken(OAuth2Credential("twitch", config.twitchOAuthToken))
        .withClientId(config.twitchClientId)
        .withClientSecret(config.twitchClientSecret)
        .withEnableHelix(true)
        .build()

    private val checker = TwitchStreamChecker(client)

    override fun check(notification: Notification): List<StreamObject> {
        return checker.checkStream(notification)
    }

    override fun isApplicable(platform: StreamingPlatform): Boolean {
        return platform == StreamingPlatform.TWITCH
    }

}

class CheckerStrategyDispatcher(config: Config) {

    private val twitch = TwitchCheckerStrategy(config)
    private val strategies = setOf(twitch)

    fun dispatch(notification: Notification): List<StreamObject> {
        val result = mutableListOf<StreamObject>()
        for (strategy in strategies) {
            if (strategy.isApplicable(notification.streamingPlatform)) {
                result += strategy.check(notification)
            }
        }
        return result
    }

}