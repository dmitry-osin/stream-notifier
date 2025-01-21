import com.github.twitch4j.TwitchClient
import com.github.twitch4j.helix.domain.Stream

sealed interface IStreamChecker {
    fun checkStream(notification: Notification): List<StreamObject>
}

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

data object VkPlayStreamChecker : IStreamChecker {
    override fun checkStream(notification: Notification): List<StreamObject> {
        throw NotImplementedError();
    }
}

data class StreamObject(val userName: String, val title: String, val gameName: String) {
    companion object {
        fun convert(stream: Stream): StreamObject =
            StreamObject(stream.userLogin, stream.title, stream.gameName)
    }
}