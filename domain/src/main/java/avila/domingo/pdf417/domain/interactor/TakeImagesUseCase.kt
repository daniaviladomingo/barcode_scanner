package avila.domingo.pdf417.domain.interactor

import avila.domingo.pdf417.domain.ICamera
import avila.domingo.pdf417.domain.interactor.type.ObservableUseCase
import avila.domingo.pdf417.domain.model.Image
import io.reactivex.Observable

class TakeImagesUseCase(private val camera: ICamera) : ObservableUseCase<Image> {
    override fun execute(): Observable<Image> = camera.images()
}