import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.concurrent.Worker
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.web.WebView
import javafx.stage.Modality
import javafx.stage.Stage
import org.apache.oltu.oauth2.client.OAuthClient
import org.apache.oltu.oauth2.client.URLConnectionClient
import org.apache.oltu.oauth2.client.request.OAuthClientRequest
import org.apache.oltu.oauth2.common.OAuthProviderType
import org.apache.oltu.oauth2.common.message.types.GrantType

class OAuthPageView(private val parent: Stage) : ChangeListener<Worker.State> {
    private var callback: ((String) -> Unit)? = null

    private val webview = WebView().apply {
        engine.load(makeAuthenticateRequest().locationUri)
        engine.loadWorker.stateProperty().addListener(this@OAuthPageView)
    }

    private val stage = Stage().apply {
        title = "TubeDL"
        initModality(Modality.APPLICATION_MODAL)
        initOwner(parent)
        val group = Group().apply {
            children.add(webview)
        }
        scene = Scene(group, 800.0, 600.0, false)
    }

    private fun makeAuthenticateRequest(): OAuthClientRequest {
        return OAuthClientRequest.authorizationProvider(OAuthProviderType.GOOGLE).apply {
            setClientId(OAuthValues.clientId)
            setRedirectURI(OAuthValues.redirect_url)
            setScope(OAuthValues.scope)
            setResponseType("code")
        }.buildQueryMessage()
    }

    private fun makeTokenRequest(code: String): OAuthClientRequest {
        return OAuthClientRequest.tokenProvider(OAuthProviderType.GOOGLE).apply {
            setClientId(OAuthValues.clientId)
            setClientSecret(OAuthValues.clientSecret)
            setRedirectURI(OAuthValues.redirect_url)
            setGrantType(GrantType.AUTHORIZATION_CODE)
            setCode(code)
        }.buildBodyMessage()
    }

    fun launch(whenFinish: (String) -> Unit) {
        callback = whenFinish
        stage.show()
    }

    override fun changed(observable: ObservableValue<out Worker.State>?, oldValue: Worker.State?, newValue: Worker.State?) {
        println(newValue)
        if (newValue == Worker.State.FAILED) {
            println("location is " + webview.engine.location)
            val code = webview.engine.location.split("code=")[1]
            println("extracted code is " + code)

            try {
                val res = OAuthClient(URLConnectionClient()).accessToken(makeTokenRequest(code), "POST")
                callback?.invoke(res.accessToken)
                println("refresh token:" + res.refreshToken)
                TokenManager().token = res.refreshToken
            } catch (e: Exception) {
            }
            stage.close()
        }
    }
}