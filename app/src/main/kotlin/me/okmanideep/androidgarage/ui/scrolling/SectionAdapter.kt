package me.okmanideep.androidgarage.ui.scrolling

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import kotterknife.bindView
import me.okmanideep.androidgarage.R
import me.okmanideep.androidgarage.ui.commons.FakeAdapter
import me.okmanideep.androidgarage.ui.commons.inflate

class SectionAdapter : RecyclerView.Adapter<SectionAdapter.ViewHolder>() {
    val DEFAULT_COUNT = 10

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bind()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
        return ViewHolder(parent!!.inflate(R.layout.item_section))
    }

    override fun getItemCount() = DEFAULT_COUNT

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rvHorizontal : RecyclerView by bindView(R.id.rv_horizontal)
        val layoutManager : LinearLayoutManager by lazy {
            LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        }
        init {
            rvHorizontal.layoutManager = layoutManager
            rvHorizontal.adapter = FakeAdapter(R.layout.item_card_hor)
        }

        fun bind() {
            layoutManager.scrollToPosition(0)
        }
    }
}