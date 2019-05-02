package avila.domingo.pdf417.ui

import avila.domingo.pdf417.base.BaseViewModel
import avila.domingo.pdf417.domain.interactor.BarCodeReaderUseCase
import avila.domingo.pdf417.domain.interactor.TakeImagesUseCase
import avila.domingo.pdf417.domain.model.Image
import avila.domingo.pdf417.schedulers.IScheduleProvider
import avila.domingo.pdf417.ui.data.Resource
import avila.domingo.pdf417.util.SingleLiveEvent

class MainActivityViewModel(
    private val imagesUseCase: TakeImagesUseCase,
    private val barCodeReaderUseCase: BarCodeReaderUseCase,
    private val scheduleProvider: IScheduleProvider
) : BaseViewModel() {

    val imagesLiveData = SingleLiveEvent<Resource<Image>>()
    val barcodeLiveData = SingleLiveEvent<Resource<String>>()

    fun images() {
        imagesLiveData.value = Resource.loading()
        addDisposable(imagesUseCase.execute()
            .observeOn(scheduleProvider.ui())
            .subscribeOn(scheduleProvider.io())
            .subscribe({ image ->
                imagesLiveData.value = Resource.success(image)
            }) {
                imagesLiveData.value = Resource.error(it.localizedMessage)
            })
    }

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