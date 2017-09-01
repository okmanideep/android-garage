package me.okmanideep.androidgarage.log

import android.util.Log
import timber.log.Timber

/**
 * Created by okmanideep on 25/01/16.
 */
class DebugTree : Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement?): String? {
        return super.createStackElementTag(element) +  ":" + (element?.lineNumber ?: "null")
    }
}

class ReleaseTree : Timber.Tree() {
    override fun isLoggable(tag: String?, priority: Int): Boolean {
        if(priority == Log.DEBUG || priority == Log.VERBOSE || priority == Log.INFO) {
            return false
        }
        return true
    }

    override fun log(priority: Int, tag: String?, message: String?, t: Throwable?) {
        Log.println(priority, tag, message)
    }
}