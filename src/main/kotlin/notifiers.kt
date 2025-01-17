import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession

sealed interface INotifier {

    fun sendNotification(message: String)

}

class TelegramNotifierBot(
    token: String,
    private val chatId: Long
) : INotifier, TelegramLongPollingBot(token) {

    init {
        TelegramBotsApi(DefaultBotSession::class.java).registerBot(this)
    }

    override fun getBotUsername(): String = "TwitchNotifierBot"

    override fun onUpdateReceived(update: Update) {
        // Обработка команд бота если потребуется
    }

    override fun sendNotification(message: String) {
        val sendMessage = SendMessage()
        sendMessage.chatId = chatId.toString()
        sendMessage.text = message
        execute(sendMessage)
    }
}

class DiscordNotifierBot(private val config: Config) : INotifier {

    override fun sendNotification(message: String) {
        throw NotImplementedError();
    }

}