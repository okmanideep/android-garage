package me.okmanideep.androidgarage.ui

import me.okmanideep.androidgarage.GarageComponent
import me.okmanideep.androidgarage.ui.activity.MainActivity
import dagger.Component
import javax.inject.Scope

@PerActivity
@Component(dependencies = arrayOf(GarageComponent::class))
interface MainScreenComponent{
    fun inject(mainActivity: MainActivity)
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class PerActivity