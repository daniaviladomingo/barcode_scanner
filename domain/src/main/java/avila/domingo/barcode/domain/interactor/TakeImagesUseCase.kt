package avila.domingo.barcode.domain.interactor

import avila.domingo.barcode.domain.ICamera
import avila.domingo.barcode.domain.interactor.type.ObservableUseCase
import avila.domingo.barcode.domain.model.Image
import io.reactivex.Observable

class TakeImagesUseCase(private val camera: ICamera) : ObservableUseCase<Image> {
    override fun execute(): Observable<Image> = camera.images()
}