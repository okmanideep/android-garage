package me.okmanideep.androidgarage.data

import java.util.*

/**
 * Created by okmanideep on 23/01/16.
 */
data class User(val name: String, val profile_image_url: String)

data class Tweet(val text: String, val user: User)

data class SearchResult(val statuses: ArrayList<Tweet>)