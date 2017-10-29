import javafx.application.Application
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.GridPane
import javafx.stage.Stage

fun main(args: Array<String>) {
    val req = YoutubeAccess().youtube.channels().list("contentDetails").setMine(true)
    req.fields = "items/contentDetails,nextPageToken,pageInfo"
    val res = req.execute()
    print(res.items)
}

class Main: Application() {
    companion object {
        fun main(args: Array<String>) {
        }
    }

    override fun start(primaryStage: Stage?) {
        if (primaryStage == null) throw NullPointerException("stage is not available")
        else {
            primaryStage.apply {
                val parent = GridPane().apply {
                    children.add(Button().apply {
                        text = "Google OAuth2認証"
                        onAction = EventHandler {
                            OAuthPageView().launch()
                        }
                    })
                }
                scene = Scene(parent, 300.0, 250.0, false)
            }.show()
        }
    }
}