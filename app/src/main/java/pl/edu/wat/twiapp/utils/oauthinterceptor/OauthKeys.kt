package pl.edu.wat.twiapp.utils.oauthinterceptor

data class OauthKeys(
    val consumerKey: String,
    val consumerSecret: String,
    val accessToken: String? = null,
    val accessSecret: String? = null
)