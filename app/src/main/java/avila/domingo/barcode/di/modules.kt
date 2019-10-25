package avila.domingo.barcode.di

import android.content.Context
import android.view.SurfaceView
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.Lifecycle
import avila.domingo.barcode.android.ILifecycleObserver
import avila.domingo.barcode.android.LifecycleManager
import avila.domingo.barcode.camera.CameraImp
import avila.domingo.barcode.camera.CameraRotationUtil
import avila.domingo.barcode.camera.INativeCamera
import avila.domingo.barcode.camera.NativeCameraManager
import avila.domingo.barcode.camera.model.mapper.CameraSideMapper
import avila.domingo.barcode.decoder.BarCodeDecoderImp
import avila.domingo.barcode.decoder.mapper.BinaryBitmapMapper
import avila.domingo.barcode.decoder.mapper.ResultMapper
import avila.domingo.barcode.domain.IBarCodeDecoder
import avila.domingo.barcode.domain.IBarCodeManager
import avila.domingo.barcode.domain.ICamera
import avila.domingo.barcode.domain.interactor.BarCodeReaderUseCase
import avila.domingo.barcode.domain.model.CameraSide
import avila.domingo.barcode.manager.BarCodeManagerImp
import avila.domingo.barcode.schedulers.IScheduleProvider
import avila.domingo.barcode.schedulers.ScheduleProviderImp
import avila.domingo.barcode.ui.MainActivityViewModel
import com.google.zxing.DecodeHintType
import com.google.zxing.Reader
import com.google.zxing.pdf417.PDF417Reader
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

val appModule = module {
    single { androidContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager }
}

val activityModule = module {
    factory { (lifecycle: Lifecycle) -> LifecycleManager(get(), lifecycle) }
}

val viewModelModule = module {
    viewModel { MainActivityViewModel(get(), get()) }
}

val useCaseModule = module {
    factory { BarCodeReaderUseCase(get()) }
}

val cameraModule = module {
    factory<ICamera> { CameraImp(get(), get()) }

    single {
        SurfaceView(get()).apply {
            layoutParams =
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
        }
    }

    single<INativeCamera> {
        NativeCameraManager(
            get(),
            get(),
            get(),
            get(),
            get()
        )
    } bind ILifecycleObserver::class

    single { CameraRotationUtil(get(), get(), get()) }

    single { CameraSide.BACK }

    single { 640..2160 }
}

val decoderModule = module {
    single<IBarCodeDecoder> { BarCodeDecoderImp(get(), get(), get(), get()) }
    single<Map<DecodeHintType, *>> {
        mapOf(
            Pair(DecodeHintType.TRY_HARDER, true),
            Pair(DecodeHintType.PURE_BARCODE, true),
            Pair(DecodeHintType.CHARACTER_SET, "ISO-8859-1")
        )
    }
    single<Reader> { PDF417Reader() } // Change this for other barcode types
}

val managerModule = module {
    factory<IBarCodeManager> { BarCodeManagerImp(get(), get(), get(), get()) }
    single { 2L }
    single { TimeUnit.SECONDS }
}

val scheduleModule = module {
    single<IScheduleProvider> { ScheduleProviderImp() }
}

val mapperModule = module {
    single { ResultMapper() }
    single { BinaryBitmapMapper() }
    single { CameraSideMapper() }
}