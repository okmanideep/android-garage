package me.okmanideep.androidgarage.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import me.okmanideep.androidgarage.R
import me.okmanideep.androidgarage.data.Tweet
import java.util.*

class TweetsAdapter : RecyclerView.Adapter<TweetViewHolder>() {
    var tweets : List<Tweet> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int {
        return tweets.size
    }

    override fun onBindViewHolder(viewHolder: TweetViewHolder?, position: Int) {
        if(viewHolder == null) return

        viewHolder.tweet.text = getItem(position).text
    }

    override fun onCreateViewHolder(parent: ViewGroup?, position: Int): TweetViewHolder? {
        val inflater = LayoutInflater.from(parent?.context)
        val itemView = inflater.inflate(R.layout.item_tweet, parent, false)
        return TweetViewHolder(itemView)
    }

    fun getItem(pos : Int) : Tweet {
        return tweets[pos]
    }
}

class TweetViewHolder(itemView: View)
    : RecyclerView.ViewHolder(itemView) {
    val dp:ImageView = itemView.findViewById(R.id.iv_dp)
    val tweet:TextView = itemView.findViewById(R.id.tv_tweet)
}
