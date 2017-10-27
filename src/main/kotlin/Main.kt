fun main(args: Array<String>) {
    val req = YoutubeAccess().youtube.channels().list("contentDetails").setMine(true)
    req.fields = "items/contentDetails,nextPageToken,pageInfo"
    val res = req.execute()
    print(res.items)
}