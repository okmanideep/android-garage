package me.okmanideep.androidgarage.base

import android.os.Bundle
import android.provider.Settings
import android.support.annotation.CallSuper
import android.support.v7.app.AppCompatActivity
import me.okmanideep.androidgarage.ObjectMap


abstract class BaseActivity: AppCompatActivity(), BaseView {
    abstract fun getPresenter(): Presenter<in BaseView>
    abstract fun getComponentKey(): String
    abstract fun createComponent(): ActivityComponent<BaseActivity>
    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (ObjectMap.has(getComponentKey())) {
            @Suppress("UNCHECKED_CAST")
            (ObjectMap.get(getComponentKey()) as ActivityComponent<BaseActivity>).inject(this)
        } else {
            val component = createComponent()
            component.inject(this)
            ObjectMap.add(getComponentKey(), component)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isActivityFinishing()) {
            ObjectMap.remove(getComponentKey())
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        getPresenter().attachView(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        getPresenter().detachView()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        getPresenter().restoreState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        getPresenter().saveState(outState)
    }

    protected fun isActivityFinishing() = isFinishing || isAlwaysFinishActivitiesEnabled()

    /**
     * To check whether "Always finish activities" or "Don't keep activities"
     * is enabled or not.
     * @return true if "Don't keep Activities" is enabled, false otherwise
     */
    protected fun isAlwaysFinishActivitiesEnabled(): Boolean {
        return Settings.Global.getInt(applicationContext.contentResolver, Settings.Global.ALWAYS_FINISH_ACTIVITIES, 0) == 1
    }

}