package com.manidesto.androidgarage

import android.app.Application
import com.manidesto.androidgarage.data.NetworkModule
import com.manidesto.androidgarage.log.DebugTree
import com.manidesto.androidgarage.log.ReleaseTree
import timber.log.Timber

class GarageApp : Application() {
    public var garageComponent : GarageComponent? = null

    override fun onCreate() {
        super.onCreate();

        //timber setup
        if(BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        } else {
            Timber.plant(ReleaseTree())
        }

        //Dagger Garage Component setup
        garageComponent = DaggerGarageComponent.builder()
                .networkModule(NetworkModule(applicationContext)).build()
    }
}