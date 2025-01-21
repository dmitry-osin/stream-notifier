/*
 * Copyright (c) 2024 Dmitry Osin <d@osin.pro>
 */

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential
import com.github.twitch4j.TwitchClientBuilder

/**
 * Interface defining checker strategy functionality.
 * Implementations of this interface are responsible for checking stream status
 * on different streaming platforms (e.g., Twitch, VK Play).
 */
sealed interface ICheckerStrategy {

    /**
     * Checks the current status of a stream based on the provided notification configuration.
     *
     * @param notification The notification configuration containing channel and platform details
     * @return List of [StreamObject] containing information about active streams.
     *         Empty list is returned if no active streams are found.
     */
    fun check(notification: Notification): List<StreamObject>

    /**
     * Checks if the strategy is applicable to a given platform.
     *
     * @param platform The platform to check against
     * @return True if the strategy is applicable to the platform, false otherwise
     */
    fun isApplicable(platform: StreamingPlatform): Boolean
}

/**
 * Enum representing different streaming platforms.
 * Each platform has a corresponding title used for configuration and identification.
 */
enum class StreamingPlatform(val title: String) {
    TWITCH("twitch"),
    VK_PLAY("vk");
}

/**
 * Implementation of [ICheckerStrategy] for Twitch streaming platform.
 * 
 * This class is responsible for checking the status of streams on Twitch.
 */
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

/**
 * Dispatcher class for handling checker strategies.
 * 
 * This class is responsible for dispatching stream checking to the appropriate strategy
 * based on the platform specified in the notification configuration.
 */
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