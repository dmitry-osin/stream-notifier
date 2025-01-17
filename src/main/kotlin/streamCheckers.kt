import com.github.twitch4j.TwitchClient
import com.github.twitch4j.helix.domain.Stream

sealed interface IStreamChecker {
    fun checkStreams(channels: List<String>): List<StreamObject>
}

class TwitchStreamChecker(
    private val twitchClient: TwitchClient
) : IStreamChecker {
    private val activeStreams = mutableSetOf<String>()

    override fun checkStreams(channels: List<String>): List<StreamObject> {
        val result = mutableListOf<Stream>()

        channels.forEach { streamer ->
            val streamList = twitchClient.helix.getStreams(
                null, null, null, 1, null, null, null, listOf(streamer)
            ).execute()

            streamList.streams.firstOrNull()?.let { streamData ->
                if (!activeStreams.contains(streamData.userId)) {
                    activeStreams.add(streamData.userId)
                    result.add(streamData)
                }
            } ?: activeStreams.remove(streamer)
        }

        return result.map { StreamObject.convert(it) }
    }
}

data object VkPlayStreamChecker : IStreamChecker {
    override fun checkStreams(channels: List<String>): List<StreamObject> {
        throw NotImplementedError();
    }
}

data class StreamObject(val userName: String, val title: String, val gameName: String) {
    companion object {
        fun convert(stream: Stream): StreamObject =
            StreamObject(stream.userLogin, stream.title, stream.gameName)
    }
}