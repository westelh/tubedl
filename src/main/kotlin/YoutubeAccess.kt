import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.YouTube

class YoutubeAccess(token: String) {
    val access: YouTube = YouTube.Builder(HttpTransporter.transport,
            JacksonFactory.getDefaultInstance(),
            GoogleCredential().setAccessToken(token)).apply {
        applicationName = "tubedl"
    }.build()
}