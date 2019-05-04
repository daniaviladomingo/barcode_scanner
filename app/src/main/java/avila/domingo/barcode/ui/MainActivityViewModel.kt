package avila.domingo.barcode.ui

import avila.domingo.barcode.base.BaseViewModel
import avila.domingo.barcode.domain.interactor.BarCodeReaderUseCase
import avila.domingo.barcode.schedulers.IScheduleProvider
import avila.domingo.barcode.ui.data.Resource
import avila.domingo.barcode.util.SingleLiveEvent

class MainActivityViewModel(
//    private val imagesUseCase: TakeImagesUseCase,
    private val barCodeReaderUseCase: BarCodeReaderUseCase,
    private val scheduleProvider: IScheduleProvider
) : BaseViewModel() {

//    val imagesLiveData = SingleLiveEvent<Resource<YUVImage>>()
    val barcodeLiveData = SingleLiveEvent<Resource<String>>()

//    fun images() {
//        imagesLiveData.value = Resource.loading()
//        addDisposable(imagesUseCase.execute()
//            .observeOn(scheduleProvider.ui())
////            .subscribeOn(scheduleProvider.io())
//            .subscribe({ image ->
//                imagesLiveData.value = Resource.success(image)
//            }) {
//                imagesLiveData.value = Resource.error(it.localizedMessage)
//            })
//    }

    fun read() {
        barcodeLiveData.value = Resource.loading()
        addDisposable(barCodeReaderUseCase.execute()
            .observeOn(scheduleProvider.ui())
            .subscribeOn(scheduleProvider.io())
            .subscribe({ info ->
                barcodeLiveData.value = Resource.success(info)
            }) {
                barcodeLiveData.value = Resource.error(it.localizedMessage)
            })
    }
}