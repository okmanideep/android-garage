package com.manidesto.androidgarage.ui.presenter

import android.os.Bundle
import com.manidesto.androidgarage.data.SearchResult
import com.manidesto.androidgarage.data.Tweet
import com.manidesto.androidgarage.data.TwitterApi
import com.manidesto.androidgarage.ui.PerActivity
import nz.bradcampbell.paperparcel.PaperParcel
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import javax.inject.Inject

interface IMainView {
    fun showLoading()
    fun showError(error : String)
    fun showTweets(tweets : List<Tweet>)
}

@PerActivity
class MainPresenter
@Inject constructor(val twitterApi: TwitterApi)
 : Callback<SearchResult> {
    private val EXPIRY_TIME = 1000*60*2
    private val BUNDLE_KEY_STATE = "bundle_key_state";

    private var view : IMainView? = null

    //STATE
    private var state = State()


    fun attachView(view : IMainView) {
        this.view = view

        if(!state.loading && tweetsExpired()) loadTweets()

        updateView()
    }

    fun detachView() {
        this.view = null
    }

    fun saveState(outState : Bundle) {
        val stateParcel = StateParcel.wrap(state)
        outState.putParcelable(BUNDLE_KEY_STATE, stateParcel)
    }

    fun restoreState(savedInstanceState : Bundle?) {
        val stateSaved = savedInstanceState?.containsKey(BUNDLE_KEY_STATE) ?: false;
        if(savedInstanceState != null && stateSaved) {
            val stateWrapper = savedInstanceState
                    .getParcelable<StateParcel>(BUNDLE_KEY_STATE)
            state = stateWrapper.contents
            state.loading = false

            updateView()
        }
    }

    fun loadTweets() {
        twitterApi.searchTweets("#BlackLivesMatter").enqueue(this)
        state.loading = true
    }

    override fun onResponse(response: Response<SearchResult>?) {
        state.loading = false
        state.loadedAt = System.currentTimeMillis()

        if(response != null) {
            if (response.isSuccess) {
                state.tweets = response.body().statuses
                state.error = null
            } else {
                state.error = "Error Code : ${response.code()}"
            }
        } else {
            state.error = "Null Response"
        }

        updateView()
    }

    override fun onFailure(t: Throwable?) {
        state.loading = false
        state.loadedAt = System.currentTimeMillis()

        state.error = t?.message ?: "Unknown Error"

        updateView()
    }

    private fun updateView() {
        val v = view ?: return
        val isError = state.error != null
        val e = state.error?:"Error"

        if(state.loading) {
            v.showLoading()
        } else if(isError) {
            v.showError(e)
        } else {
            v.showTweets(state.tweets)
        }
    }

    private fun tweetsExpired() : Boolean{
        var now = System.currentTimeMillis()
        return now - state.loadedAt > EXPIRY_TIME
    }

    @PaperParcel
    data class State (
            var loading : Boolean = false,
            var loadedAt : Long = 0,
            var tweets: ArrayList<Tweet> = ArrayList(),
            var error: String? = null
    )
}