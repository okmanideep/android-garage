package me.okmanideep.androidgarage

import me.okmanideep.androidgarage.data.NetworkModule
import me.okmanideep.androidgarage.data.TwitterApi
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(NetworkModule::class))
interface GarageComponent {
    fun provideTwitterApi() : TwitterApi
}