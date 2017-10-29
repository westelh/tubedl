import javafx.application.Application
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.web.WebView
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
                title = "TubeDL"
                val group = Group().apply {
                    children.add(WebView().apply {
                        engine.load("http://google.co.jp/")
                    })
                }
                scene = Scene(group, 300.0, 250.0, false)
            }.show()
        }
    }
}