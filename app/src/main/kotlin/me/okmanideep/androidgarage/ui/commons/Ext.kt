package me.okmanideep.androidgarage.ui.commons

import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

fun ViewGroup.inflate(@LayoutRes layoutRes: Int) : View {
    val inflater = LayoutInflater.from(context)
    return inflater.inflate(layoutRes, this, false)
}
