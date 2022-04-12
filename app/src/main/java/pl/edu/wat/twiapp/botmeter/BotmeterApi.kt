package pl.edu.wat.twiapp.botmeter

import Oauth1SigningInterceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response


class BotmeterApi {
    private val client = OkHttpClient()

    @Throws(Exception::class)
    fun getBootStats(screenName: String): BotmeterResult {
        val mediaType = "application/json".toMediaTypeOrNull()
        val body = ("{\"user\": \n" + getTwitterUser(screenName).body!!.string() + ",\n" +
                "  \"timeline\": \n" + getTwitterTimeline(screenName).body!!.string() + ",\n" +
                "  \"mentions\": \n" + getTwitterMentions(screenName).body!!.string() + "}")
            .toRequestBody(mediaType)

        val request = Request.Builder()
            .url("https://botometer-pro.p.rapidapi.com/4/check_account")
            .post(body)
            .addHeader("content-type", "application/json")
            .addHeader("X-RapidAPI-Host", "botometer-pro.p.rapidapi.com")
            .addHeader("X-RapidAPI-Key", Keys.RapidApi)
            .build()

        return BotmeterResult.fromJson(client.newCall(request).execute().body!!.string())
    }

    private fun getTwitterUser(screenName: String): Response {
        val request = Request.Builder()
            .url("https://api.twitter.com/1.1/users/show.json?screen_name=$screenName")
            .get()
            .addHeader("content-type", "application/json")
            .addHeader("authorization", "Bearer " + Keys.TwitterApiBearer)
            .build()
        return client.newCall(request).execute()
    }

    private fun getTwitterTimeline(screenName: String): Response {
        val request = Request.Builder()
            .url("https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=$screenName&count=200&include_rts=true")
            .get()
            .addHeader("content-type", "application/json")
            .addHeader("authorization", "Bearer " + Keys.TwitterApiBearer)
            .build()

        return client.newCall(request).execute()

    }

    private fun getTwitterMentions(screenName: String): Response {
        val oauth1 = Oauth1SigningInterceptor(Keys::getOauthKeys)
        val request = Request.Builder()
            .url("https://api.twitter.com/1.1/search/tweets.json?count=100&q=@$screenName")
            .get()
            .addHeader("content-type", "application/json")
            .build()

        val signed = oauth1.signRequest(request)
        return client.newCall(signed).execute()
    }
}