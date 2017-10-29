import javafx.application.Application
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.concurrent.Worker
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.web.WebView
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.Window
import org.apache.oltu.oauth2.client.OAuthClient
import org.apache.oltu.oauth2.client.URLConnectionClient
import org.apache.oltu.oauth2.client.request.OAuthClientRequest
import org.apache.oltu.oauth2.common.OAuthProviderType
import org.apache.oltu.oauth2.common.message.types.GrantType

class OAuthPageView(val owner: Main): ChangeListener<Worker.State> {
    private val clientID = "697386451833-vmi5vcs5e6kh5ufb8eopg6ea7eo8gma9.apps.googleusercontent.com"
    private val scope = "https://www.googleapis.com/auth/youtube"
    var token: String? = null

    private val webview = WebView().apply {
        engine.load(genRequest().locationUri)
        engine.loadWorker.stateProperty().addListener(this@OAuthPageView)
    }

    private val stage = Stage().apply {
        title = "TubeDL"
        val group = Group().apply {
            children.add(webview)
        }
        initModality(Modality.APPLICATION_MODAL)
        initOwner(this@OAuthPageView.owner.stage)
        scene = Scene(group, 800.0, 600.0, false)
    }

    private fun genRequest(): OAuthClientRequest {
        return OAuthClientRequest.authorizationProvider(OAuthProviderType.GOOGLE).apply {
            setClientId(clientID)
            setRedirectURI("http://localhost")
            setScope(scope)
            setResponseType("code")
        }.buildQueryMessage()
    }

    fun launch() {
        stage.show()
    }

    override fun changed(observable: ObservableValue<out Worker.State>?, oldValue: Worker.State?, newValue: Worker.State?) {
        println(newValue)
        if (newValue == Worker.State.FAILED) {
            println("location is " + webview.engine.location)
            val code = webview.engine.location.split("code=")[1]
            println("extracted code is " + code)

            val tokenReq = OAuthClientRequest.tokenProvider(OAuthProviderType.GOOGLE).apply {
                setClientId(clientID)
                setGrantType(GrantType.AUTHORIZATION_CODE)
                setCode(code)
                setClientSecret("vHGsqXiEgzc9lPCtahbTdfbY")
                setRedirectURI("http://localhost")
            }.buildBodyMessage()

            println(tokenReq.body)

            val res = OAuthClient(URLConnectionClient()).accessToken(tokenReq, "POST")
            println("token=" + res.accessToken)
            token = res.accessToken
            stage.close()
            this@OAuthPageView.owner.notifyAuthState()
        }
    }
}