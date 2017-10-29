import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.web.WebView
import javafx.stage.Stage

class OAuthPageView {
    val stage = Stage().apply {
        title = "TubeDL"
        val group = Group().apply {
            children.add(WebView().apply {
                engine.load("https://accounts.google.com/o/oauth2/auth")
            })
        }
        scene = Scene(group, 300.0, 250.0, false)
    }

    fun launch() {
        stage.show()
    }
}