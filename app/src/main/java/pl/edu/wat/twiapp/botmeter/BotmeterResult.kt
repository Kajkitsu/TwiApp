package pl.edu.wat.twiapp.botmeter

// To parse the JSON, install jackson-module-kotlin and do:
//
//   val welcome = Welcome.fromJson(jsonString)

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

val mapper = jacksonObjectMapper().apply {
    propertyNamingStrategy = PropertyNamingStrategy.LOWER_CAMEL_CASE
    setSerializationInclusion(JsonInclude.Include.NON_NULL)
}

data class BotmeterResult(
    @get:JsonProperty("message")
    @field:JsonProperty("message")
    val message: String? = null,

    @get:JsonProperty("error")
    @field:JsonProperty("error")
    val error: String? = null,


    val cap: Cap? = null,

    @get:JsonProperty("display_scores") @field:JsonProperty("display_scores")
    val displayScores: Scores? = null,

    @get:JsonProperty("raw_scores") @field:JsonProperty("raw_scores")
    val rawScores: Scores? = null,

    val user: User? = null
) {
    fun toJson() = mapper.writeValueAsString(this)

    companion object {
        fun fromJson(json: String) = mapper.readValue<BotmeterResult>(json)
    }
}


data class Cap(
    val english: Double? = null,
    val universal: Double? = null
)

data class Scores(
    val english: ScoreValues? = null,
    val universal: ScoreValues? = null
)

data class ScoreValues(
    val astroturf: Double? = null,

    @get:JsonProperty("fake_follower") @field:JsonProperty("fake_follower")
    val fakeFollower: Double? = null,

    val financial: Double? = null,
    val other: Double? = null,
    val overall: Double? = null,

    @get:JsonProperty("self_declared") @field:JsonProperty("self_declared")
    val selfDeclared: Double? = null,

    val spammer: Double? = null
)

data class User(
    @get:JsonProperty("majority_lang") @field:JsonProperty("majority_lang")
    val majorityLang: String? = null,

    @get:JsonProperty("user_data") @field:JsonProperty("user_data")
    val userData: UserData? = null
)

data class UserData(
    @get:JsonProperty("contributors_enabled") @field:JsonProperty("contributors_enabled")
    val contributorsEnabled: Boolean? = null,

    @get:JsonProperty("created_at") @field:JsonProperty("created_at")
    val createdAt: String? = null,

    @get:JsonProperty("default_profile") @field:JsonProperty("default_profile")
    val defaultProfile: Boolean? = null,

    @get:JsonProperty("default_profile_image") @field:JsonProperty("default_profile_image")
    val defaultProfileImage: Boolean? = null,

    val description: String? = null,
    val entities: UserDataEntities? = null,

    @get:JsonProperty("favourites_count") @field:JsonProperty("favourites_count")
    val favouritesCount: Long? = null,

    @get:JsonProperty("follow_request_sent") @field:JsonProperty("follow_request_sent")
    val followRequestSent: Any? = null,

    @get:JsonProperty("followers_count") @field:JsonProperty("followers_count")
    val followersCount: Long? = null,

    val following: Any? = null,

    @get:JsonProperty("friends_count") @field:JsonProperty("friends_count")
    val friendsCount: Long? = null,

    @get:JsonProperty("geo_enabled") @field:JsonProperty("geo_enabled")
    val geoEnabled: Boolean? = null,

    @get:JsonProperty("has_extended_profile") @field:JsonProperty("has_extended_profile")
    val hasExtendedProfile: Boolean? = null,

    val id: Long? = null,

    @get:JsonProperty("id_str") @field:JsonProperty("id_str")
    val idStr: String? = null,

    @get:JsonProperty("is_translation_enabled") @field:JsonProperty("is_translation_enabled")
    val isTranslationEnabled: Boolean? = null,

    @get:JsonProperty("is_translator") @field:JsonProperty("is_translator")
    val isTranslator: Boolean? = null,

    val lang: Any? = null,

    @get:JsonProperty("listed_count") @field:JsonProperty("listed_count")
    val listedCount: Long? = null,

    val location: String? = null,
    val name: String? = null,
    val notifications: Any? = null,

    @get:JsonProperty("profile_background_color") @field:JsonProperty("profile_background_color")
    val profileBackgroundColor: String? = null,

    @get:JsonProperty("profile_background_image_url") @field:JsonProperty("profile_background_image_url")
    val profileBackgroundImageURL: String? = null,

    @get:JsonProperty("profile_background_image_url_https") @field:JsonProperty("profile_background_image_url_https")
    val profileBackgroundImageURLHTTPS: String? = null,

    @get:JsonProperty("profile_background_tile") @field:JsonProperty("profile_background_tile")
    val profileBackgroundTile: Boolean? = null,

    @get:JsonProperty("profile_banner_url") @field:JsonProperty("profile_banner_url")
    val profileBannerURL: String? = null,

    @get:JsonProperty("profile_image_url") @field:JsonProperty("profile_image_url")
    val profileImageURL: String? = null,

    @get:JsonProperty("profile_image_url_https") @field:JsonProperty("profile_image_url_https")
    val profileImageURLHTTPS: String? = null,

    @get:JsonProperty("profile_link_color") @field:JsonProperty("profile_link_color")
    val profileLinkColor: String? = null,

    @get:JsonProperty("profile_location") @field:JsonProperty("profile_location")
    val profileLocation: Any? = null,

    @get:JsonProperty("profile_sidebar_border_color") @field:JsonProperty("profile_sidebar_border_color")
    val profileSidebarBorderColor: String? = null,

    @get:JsonProperty("profile_sidebar_fill_color") @field:JsonProperty("profile_sidebar_fill_color")
    val profileSidebarFillColor: String? = null,

    @get:JsonProperty("profile_text_color") @field:JsonProperty("profile_text_color")
    val profileTextColor: String? = null,

    @get:JsonProperty("profile_use_background_image") @field:JsonProperty("profile_use_background_image")
    val profileUseBackgroundImage: Boolean? = null,

    val protected: Boolean? = null,

    @get:JsonProperty("screen_name") @field:JsonProperty("screen_name")
    val screenName: String? = null,

    val status: Status? = null,

    @get:JsonProperty("statuses_count") @field:JsonProperty("statuses_count")
    val statusesCount: Long? = null,

    @get:JsonProperty("time_zone") @field:JsonProperty("time_zone")
    val timeZone: Any? = null,

    @get:JsonProperty("translator_type") @field:JsonProperty("translator_type")
    val translatorType: String? = null,

    val url: Any? = null,

    @get:JsonProperty("utc_offset") @field:JsonProperty("utc_offset")
    val utcOffset: Any? = null,

    val verified: Boolean? = null,

    @get:JsonProperty("withheld_in_countries") @field:JsonProperty("withheld_in_countries")
    val withheldInCountries: List<Any?>? = null
)

data class UserDataEntities(
    val description: Description? = null
)

data class Description(
    val urls: List<Any?>? = null
)

data class Status(
    val contributors: Any? = null,
    val coordinates: Any? = null,

    @get:JsonProperty("created_at") @field:JsonProperty("created_at")
    val createdAt: String? = null,

    val entities: StatusEntities? = null,

    @get:JsonProperty("favorite_count") @field:JsonProperty("favorite_count")
    val favoriteCount: Long? = null,

    val favorited: Boolean? = null,
    val geo: Any? = null,
    val id: Double? = null,

    @get:JsonProperty("id_str") @field:JsonProperty("id_str")
    val idStr: String? = null,

    @get:JsonProperty("in_reply_to_screen_name") @field:JsonProperty("in_reply_to_screen_name")
    val inReplyToScreenName: String? = null,

    @get:JsonProperty("in_reply_to_status_id") @field:JsonProperty("in_reply_to_status_id")
    val inReplyToStatusID: Double? = null,

    @get:JsonProperty("in_reply_to_status_id_str") @field:JsonProperty("in_reply_to_status_id_str")
    val inReplyToStatusIDStr: String? = null,

    @get:JsonProperty("in_reply_to_user_id") @field:JsonProperty("in_reply_to_user_id")
    val inReplyToUserID: Long? = null,

    @get:JsonProperty("in_reply_to_user_id_str") @field:JsonProperty("in_reply_to_user_id_str")
    val inReplyToUserIDStr: String? = null,

    @get:JsonProperty("is_quote_status") @field:JsonProperty("is_quote_status")
    val isQuoteStatus: Boolean? = null,

    val lang: String? = null,
    val place: Any? = null,

    @get:JsonProperty("retweet_count") @field:JsonProperty("retweet_count")
    val retweetCount: Long? = null,

    val retweeted: Boolean? = null,
    val source: String? = null,
    val text: String? = null,
    val truncated: Boolean? = null
)

data class StatusEntities(
    val hashtags: List<Any?>? = null,
    val symbols: List<Any?>? = null,
    val urls: List<Any?>? = null,

    @get:JsonProperty("user_mentions") @field:JsonProperty("user_mentions")
    val userMentions: List<UserMention>? = null
)

data class UserMention(
    val id: Long? = null,

    @get:JsonProperty("id_str") @field:JsonProperty("id_str")
    val idStr: String? = null,

    val indices: List<Long>? = null,
    val name: String? = null,

    @get:JsonProperty("screen_name") @field:JsonProperty("screen_name")
    val screenName: String? = null
)

