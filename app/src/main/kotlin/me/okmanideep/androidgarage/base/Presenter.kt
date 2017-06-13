package me.okmanideep.androidgarage.base

import android.os.Bundle


abstract class Presenter<V> {
    private var attachedView : V? = null

    fun attachView(view: V) {
        this.attachedView = view
        onEnter(view)
    }

    fun detachView() {
        this.attachedView = null
        onExit()
    }

    open fun saveState(savedInstanceState: Bundle) {}
    open fun restoreState(savedInstanceState: Bundle) {}
    abstract fun onEnter(view: V)
    open fun onExit() {}
}