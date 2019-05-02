package avila.domingo.pdf417

import android.app.Application
import avila.domingo.pdf417.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AppApplication)
            androidLogger()
            modules(
                appModule,
                activityModule,
                viewModelModule,
                useCaseModule,
                managerModule,
                decoderModule,
                cameraModule,
                scheduleModule,
                mapperModule
            )
        }
    }
}