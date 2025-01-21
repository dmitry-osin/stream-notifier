/*
 * Copyright (c) 2024 Dmitry Osin <d@osin.pro>
 */

import com.github.twitch4j.TwitchClient
import com.github.twitch4j.helix.domain.Stream

/**
 * Interface defining stream checking functionality.
 * Implementations of this interface are responsible for checking stream status
 * on different streaming platforms (e.g., Twitch, VK Play).
 */
sealed interface IStreamChecker {
    /**
     * Checks the current status of a stream based on the provided notification configuration.
     *
     * @param notification The notification configuration containing channel and platform details
     * @return List of [StreamObject] containing information about active streams.
     *         Empty list is returned if no active streams are found.
     */
    fun checkStream(notification: Notification): List<StreamObject>
}

/**
 * Implementation of [IStreamChecker] for Twitch streaming platform.
 * 
 * This class is responsible for checking the status of streams on Twitch.
 */
class TwitchStreamChecker(
    private val twitchClient: TwitchClient
) : IStreamChecker {
    private val activeStreams = mutableSetOf<String>()

    override fun checkStream(notification: Notification): List<StreamObject> {
        val result = mutableListOf<Stream>()

        val streamList = twitchClient.helix.getStreams(
            null, null, null, 1, null, null, null, listOf(notification.channel)
        ).execute()

        streamList.streams.firstOrNull()?.let { streamData ->
            if (streamData.userId !in activeStreams) {
                activeStreams.add(streamData.userId)
                result.add(streamData)
            }
        } ?: activeStreams.remove(notification.channel)

        return result.map { StreamObject.convert(it) }
    }
}

/**
 * Implementation of [IStreamChecker] for VK Play streaming platform.
 * 
 * This class is responsible for checking the status of streams on VK Play.
 */
data object VkPlayStreamChecker : IStreamChecker {
    override fun checkStream(notification: Notification): List<StreamObject> {
        throw NotImplementedError();
    }
}

/**
 * Data class representing a stream object with user name, title, and game name.
 * 
 * This class is used to store and represent information about a stream.
 */
data class StreamObject(val userName: String, val title: String, val gameName: String) {
    companion object {
        fun convert(stream: Stream): StreamObject =
            StreamObject(stream.userLogin, stream.title, stream.gameName)
    }
}