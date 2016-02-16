package com.manidesto.androidgarage.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.manidesto.androidgarage.R
import com.manidesto.androidgarage.data.SearchResult
import com.manidesto.androidgarage.data.TwitterApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Callback
import retrofit2.GsonConverterFactory
import retrofit2.Response
import retrofit2.Retrofit


class MainActivity : AppCompatActivity(), Callback<SearchResult> {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder().addInterceptor(logger).build()

        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.twitter.com/1.1/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val twitterApi = retrofit.create(TwitterApi::class.java)

        val call = twitterApi.searchTweets("#BlackLivesMatter")
        call.enqueue(this)
    }

    override fun onFailure(t: Throwable?) {
        Toast.makeText(this, t?.message, Toast.LENGTH_LONG).show()
    }

    override fun onResponse(response: Response<SearchResult>?) {
        val tweets = response?.body()?.statuses;
        Toast.makeText(this, (tweets?.size ?: "No tweets").toString(), Toast.LENGTH_LONG).show()
    }
}
