package levkaantonov.com.study.recorder

import android.app.Application
import android.content.Context
import levkaantonov.com.study.recorder.di.AppComponent
import levkaantonov.com.study.recorder.di.DaggerAppComponent

class RecorderApp : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is RecorderApp -> appComponent
        else -> this.applicationContext.appComponent
    }