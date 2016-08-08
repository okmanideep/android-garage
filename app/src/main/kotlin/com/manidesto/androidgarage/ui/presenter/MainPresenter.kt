package com.manidesto.androidgarage.ui.presenter

import android.os.Bundle
import com.manidesto.androidgarage.data.SearchResult
import com.manidesto.androidgarage.data.Tweet
import com.manidesto.androidgarage.data.TwitterApi
import com.manidesto.androidgarage.ui.PerActivity
import dagger.Lazy
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import javax.inject.Inject

interface IMainView {
    fun setLoading(loading : Boolean)
    fun setError(error : String?)
    fun setTweets(tweets : List<Tweet>)
}

@PerActivity
class MainPresenter
@Inject constructor(val lazyTwitterApi: Lazy<TwitterApi>)
 : Callback<SearchResult> {
    private val EXPIRY_TIME = 1000*60*2
    private val BUNDLE_KEY_STATE = "bundle_key_state";

    private var view : IMainView? = null

    //STATE
    private var state = State()


    fun attachView(view : IMainView) {
        this.view = view

        if(!state.loading && tweetsExpired()) {
            state.loading = true
            loadTweets()
        }

        updateView()
    }

    fun detachView() {
        this.view = null
    }

    fun saveState(outState : Bundle) {
    }

    fun restoreState(savedInstanceState : Bundle?) {
    }

    fun onRefresh() {
        state.loading = true
        loadTweets()
    }

    override fun onResponse(call: Call<SearchResult>?, response: Response<SearchResult>?) {
        state.loading = false
        state.loadedAt = System.currentTimeMillis()

        if(response != null) {
            when {
                response.isSuccessful() -> {
                    state.tweets = response.body().statuses
                    state.error = null
                }
                response.code() == 401 -> {
                    state.error = "App not compiled with valid TWITTER_BEARER_TOKEN"
                }
                else -> {
                    state.error = "Error Code : ${response.code()}"
                }
            }
        } else {
            state.error = "Null Response"
        }

        updateView()
    }

    override fun onFailure(call: Call<SearchResult>?, t: Throwable?) {
        state.loading = false
        state.loadedAt = System.currentTimeMillis()

        state.error = t?.message ?: "Unknown Error"

        updateView()
    }

    private fun updateView() {
        val v = view ?: return

        v.setLoading(state.loading)
        v.setError(state.error)
        v.setTweets(state.tweets)
    }

    private fun tweetsExpired() : Boolean{
        var now = System.currentTimeMillis()
        return now - state.loadedAt > EXPIRY_TIME
    }

    private fun loadTweets() {
        Thread(){
            run {
                lazyTwitterApi.get().searchTweets("#BlackLivesMatter").enqueue(this)
            }
        }.run()
    }

    data class State (
            var loading : Boolean = false,
            var loadedAt : Long = 0,
            var tweets: ArrayList<Tweet> = ArrayList(),
            var error: String? = null
    )
}