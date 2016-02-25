package com.manidesto.androidgarage.ui

import com.manidesto.androidgarage.GarageComponent
import com.manidesto.androidgarage.ui.activity.MainActivity
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