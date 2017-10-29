import javafx.application.Application
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.GridPane
import javafx.stage.Stage

class Main: Application() {
    var youtube: YoutubeAccess? = null

    private val authButton = Button().apply {
        text = "Google OAuth2認証"
    }

    private val checkButton = Button().apply {
        text = "認証状態の確認"
        onAction = EventHandler {
            notifyAuthState()
        }
    }

    private val stateLabel = Label().apply {
        text = "Youtube API Connection: N/A"
    }

    override fun start(primaryStage: Stage?) {
        if (primaryStage == null) throw NullPointerException("stage is not available")
        else {
            authButton.onAction = EventHandler {
                OAuthPageView(primaryStage).launch { token ->
                    youtube = YoutubeAccess(token)
                    notifyAuthState()
                }
            }
            primaryStage.apply {
                val parent = GridPane().apply {
                    add(authButton, 0, 1)
                    add(checkButton, 0, 3)
                    add(stateLabel, 0, 4)
                }
                scene = Scene(parent, 300.0, 250.0, false)
            }.show()
        }
    }

    private fun notifyAuthState() {
        Alert(Alert.AlertType.INFORMATION).apply {
            contentText = "Google OAuth2認証は" + if (youtube == null) "完了していません" else "確認済みです"
        }.show()
    }
}