import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.web.WebView
import javafx.stage.Stage

class OAuthPageView {
    val stage = Stage().apply {
        title = "TubeDL"
        val group = Group().apply {
            children.add(WebView().apply {
                engine.load("http://google.co.jp/")
            })
        }
        scene = Scene(group, 300.0, 250.0, false)
    }

    fun launch() {
        stage.show()
    }
}