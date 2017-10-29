import com.google.api.services.youtube.model.PlaylistItem
import javafx.application.Application
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.beans.property.StringProperty
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.layout.FlowPane
import javafx.scene.layout.GridPane
import javafx.stage.Stage

class Main: Application() {
    var youtube: YoutubeAccess? = null

    lateinit var table: TableView<PlaylistItemWrapper>

    private val authButton = Button().apply {
        text = "Google OAuth2認証"
        isDisable = true
    }

    private val checkButton = Button().apply {
        text = "認証状態の確認"
        onAction = EventHandler {
            notifyAuthState()
        }
    }

    private val stateLabel = Label().apply {
        text = "Youtube API Connection:N/A"
    }

    private val callAPI = Button().apply {
        text = "call youtube data api"
        onAction = EventHandler {
            youtube?.let {
                val id = it.access.channels().list("contentDetails").apply {
                    mine = true
                    fields = "items/contentDetails,nextPageToken,pageInfo"
                }.execute().items[0].contentDetails.relatedPlaylists.likes
                val request = it.access.playlistItems().list("id,contentDetails,snippet").apply {
                    fields = "items(contentDetails/videoId,snippet/title,snippet/publishedAt),nextPageToken,pageInfo"
                    playlistId = id
                }

                val list = mutableListOf<PlaylistItemWrapper>()
                table.items = FXCollections.observableList(list)
                var nextPageToken: String? = ""
                do {
                    val item = request.setPageToken(nextPageToken).execute()
                    for (i in item.items) {
                        list.add(PlaylistItemWrapper(i))
                        println(i)
                    }
                    nextPageToken = item.nextPageToken
                } while (nextPageToken != null)
            }
        }
    }

    override fun start(primaryStage: Stage) {
        table = TableView()
        table.apply {
            columns.setAll(TableColumn<PlaylistItemWrapper, String>("ID").apply {
                cellValueFactory = PropertyValueFactory<PlaylistItemWrapper, String>("id")
            }, TableColumn<PlaylistItemWrapper, String>("Title").apply {
                cellValueFactory = PropertyValueFactory<PlaylistItemWrapper, String>("title")
            })
        }
        primaryStage.apply {
            val parent = FlowPane().apply {
                vgap = 4.0
                hgap = 1.0
                children.add(GridPane().apply {
                    add(authButton, 0, 0)
                    add(checkButton, 0, 1)
                    add(stateLabel, 0, 2)
                    add(callAPI, 0, 3)
                })
                children.add(GridPane().apply {
                    add(table, 0, 0)
                })
            }
            scene = Scene(parent, 600.0, 400.0, false)
        }.show()

        with(TokenManager()) {
            try {
                youtube = YoutubeAccess(execute(makeRefreshRequest()))
                updateState()
            } catch (e: IllegalStateException) {
                OAuthPageView(primaryStage).launch { token ->
                    youtube = YoutubeAccess(token)
                    updateState()
                }
            }
        }
    }

    private fun notifyAuthState() {
        Alert(Alert.AlertType.INFORMATION).apply {
            contentText = "Google OAuth2認証は" + if (youtube == null) "完了していません" else "確認済みです"
        }.show()
    }

    private fun updateState() {
        stateLabel.text = "Youtube API Connection:" + if (youtube == null) "N/A" else "Ready"
    }

    class PlaylistItemWrapper(private val item: PlaylistItem) {
        fun idProperty(): StringProperty {
            return ReadOnlyStringWrapper(item.contentDetails.videoId)
        }

        fun titleProperty(): StringProperty {
            return ReadOnlyStringWrapper(item.snippet.title)
        }
    }
}