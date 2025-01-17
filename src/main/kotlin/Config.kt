import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.File

data class Config(
    val twitchClientId: String,
    val twitchClientSecret: String,
    val telegramToken: String,
    val telegramChatId: Long,
    val notificationDelayInSeconds: Long,
    val locale: String,
    val botName: String
) {
    companion object {
        fun loadConfig(configPath: String = CONFIG_FILE): Config {
            val mapper = ObjectMapper().registerModule(
                KotlinModule.Builder()
                    .withReflectionCacheSize(512)
                    .configure(KotlinFeature.NullToEmptyCollection, false)
                    .configure(KotlinFeature.NullToEmptyMap, false)
                    .configure(KotlinFeature.NullIsSameAsDefault, false)
                    .configure(KotlinFeature.SingletonSupport, true)
                    .configure(KotlinFeature.StrictNullChecks, false)
                    .build()
            )
            return mapper.readValue(File(configPath), Config::class.java)
        }
    }
} 