package avila.domingo.barcode.di

import android.content.Context
import android.view.SurfaceView
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import avila.domingo.barcode.camera.CameraImp
import avila.domingo.barcode.camera.model.mapper.AllImageMapper
import avila.domingo.barcode.decoder.BarCodeDecoderImp
import avila.domingo.barcode.decoder.mapper.ResultMapper
import avila.domingo.barcode.di.qualifiers.ForActivity
import avila.domingo.barcode.di.qualifiers.ForApplication
import avila.domingo.barcode.domain.IBarCodeDecoder
import avila.domingo.barcode.domain.IBarCodeManager
import avila.domingo.barcode.domain.ICamera
import avila.domingo.barcode.domain.interactor.BarCodeReaderUseCase
import avila.domingo.barcode.domain.interactor.TakeImagesUseCase
import avila.domingo.barcode.manager.BarCodeManagerImp
import avila.domingo.barcode.schedulers.IScheduleProvider
import avila.domingo.barcode.schedulers.ScheduleProviderImp
import avila.domingo.barcode.ui.MainActivityViewModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.DecodeHintType
import com.google.zxing.Reader
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
    viewModel { MainActivityViewModel(get(), get()/*, get()*/) }
}

val useCaseModule = module {
//    factory { TakeImagesUseCase(get()) }
    factory { BarCodeReaderUseCase(get()) }
}

val cameraModule = module {
    factory<ICamera> { CameraImp(get(), get(), get(), get(), get()) }

    single {
        SurfaceView(get()).apply {
            layoutParams =
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }
    single { 2L }
    single { TimeUnit.SECONDS }
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