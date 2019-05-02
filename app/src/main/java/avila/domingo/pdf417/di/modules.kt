package avila.domingo.pdf417.di

import android.content.Context
import android.view.SurfaceView
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import avila.domingo.pdf417.camera.CameraImp
import avila.domingo.pdf417.camera.model.CameraImage
import avila.domingo.pdf417.camera.model.mapper.AllImageMapper
import avila.domingo.pdf417.decoder.BarCodeDecoderImp
import avila.domingo.pdf417.decoder.mapper.ResultMapper
import avila.domingo.pdf417.di.qualifiers.ForActivity
import avila.domingo.pdf417.di.qualifiers.ForApplication
import avila.domingo.pdf417.domain.IBarCodeDecoder
import avila.domingo.pdf417.domain.IBarCodeManager
import avila.domingo.pdf417.domain.ICamera
import avila.domingo.pdf417.domain.interactor.BarCodeReaderUseCase
import avila.domingo.pdf417.domain.interactor.TakeImagesUseCase
import avila.domingo.pdf417.domain.model.Image
import avila.domingo.pdf417.domain.model.mapper.Mapper
import avila.domingo.pdf417.manager.BarCodeManagerImp
import avila.domingo.pdf417.schedulers.IScheduleProvider
import avila.domingo.pdf417.schedulers.ScheduleProviderImp
import avila.domingo.pdf417.ui.MainActivityViewModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.DecodeHintType
import com.google.zxing.Reader
import com.google.zxing.Result
import com.google.zxing.pdf417.PDF417Reader
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

val appModule = module {
    single(ForApplication) { androidContext() }
    single { (get(ForApplication) as Context).getSystemService(Context.WINDOW_SERVICE) as WindowManager }
}

val activityModule = module {
    lateinit var activityReference: AppCompatActivity
    factory { (activity: AppCompatActivity) -> activityReference = activity }
    factory<Context>(ForActivity) { activityReference }
}

val viewModelModule = module {
    viewModel { MainActivityViewModel(get(), get(), get()) }
}

val useCaseModule = module {
    factory { TakeImagesUseCase(get()) }
    factory { BarCodeReaderUseCase(get()) }
}

val cameraModule = module {
    factory<ICamera> { CameraImp(get(), get(), get(), get()) }

    single {
        SurfaceView(get()).apply {
            layoutParams =
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }
    single { TimeUnit.SECONDS.toMillis(2) }
}

val decoderModule = module {
    single<IBarCodeDecoder> { BarCodeDecoderImp(get(), get(), get()) }
    single<Map<DecodeHintType, *>> {
        mapOf(
            Pair(DecodeHintType.TRY_HARDER, true),
            Pair(DecodeHintType.PURE_BARCODE, true),
            Pair(DecodeHintType.CHARACTER_SET, "ISO-8859-1"),
            Pair(DecodeHintType.POSSIBLE_FORMATS, listOf(BarcodeFormat.PDF_417))
        )
    }
    single<Reader> { PDF417Reader() } // Change this for other barcode types
}

val managerModule = module {
    single<IBarCodeManager> { BarCodeManagerImp(get(), get()) }
}

val scheduleModule = module {
    single<IScheduleProvider> { ScheduleProviderImp() }
}

val mapperModule = module {
    single { ResultMapper() }
    single { AllImageMapper() }
}