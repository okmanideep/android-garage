package me.okmanideep.androidgarage.base


interface ActivityComponent<in A: BaseActivity> {
    fun inject(activity: A)
}