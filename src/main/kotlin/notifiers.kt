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
    private val chatId: String
) : INotifier, TelegramLongPollingBot(token) {

    init {
        TelegramBotsApi(DefaultBotSession::class.java).registerBot(this)
    }

    override fun getBotUsername(): String = "TwitchNotifierBot"

    override fun onUpdateReceived(update: Update) {

    }

    override fun sendNotification(message: String) {
        val sendMessage = SendMessage()
        sendMessage.chatId = chatId
        sendMessage.text = message
        execute(sendMessage)
    }
}