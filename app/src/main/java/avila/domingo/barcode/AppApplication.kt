package avila.domingo.barcode

import android.app.Application
import avila.domingo.barcode.di.*
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
                decoderModule,
                cameraModule,
                dispatcherModule,
                mapperModule
            )
        }
    }
}