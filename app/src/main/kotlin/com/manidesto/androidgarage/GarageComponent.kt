package com.manidesto.androidgarage

import com.manidesto.androidgarage.data.NetworkModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(NetworkModule::class))
interface GarageComponent {
}