import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.YouTube

class YoutubeAccess {
    private val keyFile = ClassLoader.getSystemResourceAsStream("tubedl-key.json")

    val youtube: YouTube = YouTube.Builder(HttpTransporter.transport,
            JacksonFactory.getDefaultInstance(),
            GoogleCredential.fromStream(keyFile).apply {
        accessToken = "AIzaSyDWOm3mIVbW0rD9Z6YDJX5gnY7h_pLNC-4"
    }.createScoped(arrayListOf("https://www.googleapis.com/auth/youtube.readonly"))).apply {
        applicationName = "tubedl"
    }.build()
}