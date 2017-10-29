import javafx.application.Application
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}