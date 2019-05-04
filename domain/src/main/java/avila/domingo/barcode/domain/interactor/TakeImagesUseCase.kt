package avila.domingo.barcode.domain.interactor

import avila.domingo.barcode.domain.ICamera
import avila.domingo.barcode.domain.interactor.type.SingleUseCase
import avila.domingo.barcode.domain.model.YUVImage
import io.reactivex.Single

class TakeImagesUseCase(private val camera: ICamera) : SingleUseCase<YUVImage> {
    override fun execute(): Single<YUVImage> = camera.getImage()
}