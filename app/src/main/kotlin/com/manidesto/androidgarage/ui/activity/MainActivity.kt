package com.manidesto.androidgarage.ui.activity

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.manidesto.androidgarage.GarageApp
import com.manidesto.androidgarage.ObjectMap
import com.manidesto.androidgarage.R
import com.manidesto.androidgarage.data.Tweet
import com.manidesto.androidgarage.ui.DaggerMainScreenComponent
import com.manidesto.androidgarage.ui.MainScreenComponent
import com.manidesto.androidgarage.ui.adapter.TweetsAdapter
import com.manidesto.androidgarage.ui.presenter.IMainView
import com.manidesto.androidgarage.ui.presenter.MainPresenter
import javax.inject.Inject


class MainActivity : AppCompatActivity(), IMainView{
    val mainScreenKey = MainScreenComponent::class.java.name
    @Inject lateinit var presenter : MainPresenter

    lateinit var tweetsView : RecyclerView
    lateinit var swipeRefreshLayout : SwipeRefreshLayout
    lateinit var adapter : TweetsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainScreenComponent = if(ObjectMap.has(mainScreenKey)) {
            ObjectMap.get(mainScreenKey) as MainScreenComponent
        } else {
            buildMainScreenComponent()
        }
        ObjectMap.add(mainScreenKey, mainScreenComponent)

        mainScreenComponent.inject(this)
        initViews()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.attachView(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.detachView()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        if(outState != null && isFinishing) presenter.saveState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        presenter.restoreState(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        if(isFinishing) ObjectMap.remove(mainScreenKey)
    }

    //------------IMainView-------------------------
    override fun setLoading(loading : Boolean) {
        swipeRefreshLayout.isRefreshing = loading
    }

    override fun setError(error: String?) {
        if(error != null)
            Toast.makeText(this, error, Toast.LENGTH_LONG).show()
    }

    override fun setTweets(tweets: List<Tweet>) {
        adapter.tweets = tweets
    }
    //-----------end IMainView----------------------

    private fun buildMainScreenComponent() : MainScreenComponent {
        val app = application as GarageApp

        return DaggerMainScreenComponent.builder()
                .garageComponent(app.garageComponent)
                .build()
    }

    private fun initViews() {
        tweetsView = findViewById(R.id.rv_tweets) as RecyclerView
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout) as SwipeRefreshLayout

        adapter = TweetsAdapter()
        tweetsView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        tweetsView.adapter = adapter

        swipeRefreshLayout.setOnRefreshListener { presenter.onRefresh() }
    }
}
