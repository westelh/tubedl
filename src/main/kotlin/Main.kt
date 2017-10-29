import javafx.application.Application
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.GridPane
import javafx.stage.Stage

class Main: Application() {
    lateinit var auth: OAuthPageView
    lateinit var stage: Stage

    companion object {
        fun main(args: Array<String>) {
        }
    }

    override fun start(primaryStage: Stage?) {
        if (primaryStage == null) throw NullPointerException("stage is not available")
        stage = primaryStage
        auth = OAuthPageView(this)
        stage.apply {
            val parent = GridPane().apply {
                add(Button().apply {
                    text = "Google OAuth2認証"
                    onAction = EventHandler {
                        auth.launch()
                    }
                }, 0, 1)
                add(Button().apply {
                    text = "print token"
                    onAction = EventHandler {
                        println(auth.token?:"have not received")
                    }
                }, 0, 2)
                add(Button().apply {
                    text = "認証状態の確認"
                    onAction = EventHandler {
                        notifyAuthState()
                    }
                }, 0, 3)
                add(Label().apply {
                    text = "Youtube API Connection: N/A"
                }, 0, 4)
            }
            scene = Scene(parent, 300.0, 250.0, false)
        }.show()
    }

    fun notifyAuthState() {
        Alert(Alert.AlertType.INFORMATION).apply {
            contentText = "Google OAuth2認証は" + if (auth.token==null) "完了していません" else "確認済みです"
        }.show()
    }
}