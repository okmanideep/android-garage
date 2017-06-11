package me.okmanideep.androidgarage

import android.app.Application
import me.okmanideep.androidgarage.data.NetworkModule
import me.okmanideep.androidgarage.log.DebugTree
import me.okmanideep.androidgarage.log.ReleaseTree
import timber.log.Timber

class GarageApp : Application() {
    var garageComponent : GarageComponent? = null

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