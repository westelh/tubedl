import org.apache.oltu.oauth2.client.OAuthClient
import org.apache.oltu.oauth2.client.URLConnectionClient
import org.apache.oltu.oauth2.client.request.OAuthClientRequest
import org.apache.oltu.oauth2.common.OAuthProviderType
import org.apache.oltu.oauth2.common.message.types.GrantType
import java.io.*

class TokenManager {
    var token: String
        get() {
            return try {
                BufferedReader(FileReader("refresh_token")).readLine()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                ""
            }
        }
        set(value) {
            try {
                BufferedWriter(FileWriter("refresh_token", true)).write(value)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

    fun makeRefreshRequest(): OAuthClientRequest {
        if (token.isEmpty()) throw IllegalStateException("Refresh token is N/A. Cannot make.")
        else {
            return OAuthClientRequest.tokenProvider(OAuthProviderType.GOOGLE).apply {
                setClientId(OAuthValues.clientId)
                setClientSecret(OAuthValues.clientSecret)
                setGrantType(GrantType.REFRESH_TOKEN)
                setRefreshToken(token)
            }.buildBodyMessage().apply {
                println(this.body)
            }
        }
    }

    fun execute(request: OAuthClientRequest): String {
        return OAuthClient(URLConnectionClient()).accessToken(request).accessToken
    }
}