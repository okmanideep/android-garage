package me.okmanideep.androidgarage.ui.scrolling

import android.content.Context
import android.util.AttributeSet
import me.okmanideep.androidgarage.ui.commons.BetterRecyclerView


class FeedRootRecyclerView : BetterRecyclerView {
    constructor(context : Context?) : this(context, null){}

    constructor(context: Context?, attrs : AttributeSet?) : this(context, attrs, 0){}

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr : Int) : super(context, attrs, defStyleAttr) {}

    override fun requestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        // do nothing
    }
}