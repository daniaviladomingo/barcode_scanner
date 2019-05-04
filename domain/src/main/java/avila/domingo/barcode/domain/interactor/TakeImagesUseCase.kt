package avila.domingo.barcode.domain.interactor

import avila.domingo.barcode.domain.ICamera
import avila.domingo.barcode.domain.interactor.type.SingleUseCase
import avila.domingo.barcode.domain.model.Image
import io.reactivex.Single

class TakeImagesUseCase(private val camera: ICamera) : SingleUseCase<Image> {
    override fun execute(): Single<Image> = camera.getImage()
}