package avila.domingo.pdf417.di

import android.content.Context
import android.view.SurfaceView
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import avila.domingo.pdf417.ui.MainActivityViewModel
import avila.domingo.pdf417.camera.CameraImp
import avila.domingo.pdf417.di.qualifiers.ForActivity
import avila.domingo.pdf417.di.qualifiers.ForApplication
import avila.domingo.pdf417.domain.ICamera
import avila.domingo.pdf417.domain.interactor.TakeImagesUseCase
import avila.domingo.pdf417.schedulers.IScheduleProvider
import avila.domingo.pdf417.schedulers.ScheduleProviderImp
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

val appModule = module {
    single(ForApplication) { androidContext() }
    single{(get(ForApplication) as Context).getSystemService(Context.WINDOW_SERVICE) as WindowManager }
}

val activityModule = module {
    lateinit var activityReference: AppCompatActivity
    factory { (activity: AppCompatActivity) -> activityReference = activity}
    factory<Context>(ForActivity) { activityReference }
}

val viewModelModule = module {
    viewModel { MainActivityViewModel(get(), get()) }
}

val useCaseModule = module {
    factory { TakeImagesUseCase(get()) }
}

val cameraModule = module {
    factory<ICamera> { CameraImp(get(), get(), get()) }

    single {
        SurfaceView(get()).apply {
            layoutParams =
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }
    single { TimeUnit.SECONDS.toMillis(2) }
}

val scheduleModule = module {
    single<IScheduleProvider> { ScheduleProviderImp() }
}