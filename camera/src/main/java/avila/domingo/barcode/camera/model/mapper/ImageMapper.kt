package avila.domingo.barcode.camera.model.mapper

import android.graphics.YuvImage
import avila.domingo.barcode.camera.model.CameraImage
import avila.domingo.barcode.domain.model.YUVImage
import avila.domingo.barcode.domain.model.mapper.Mapper

class ImageMapper : Mapper<CameraImage, YUVImage>() {
    override fun map(model: CameraImage): YUVImage = model.run {
        YuvImage(image, imageFormat, width, height, null).run {
            YUVImage(yuvData, width, height)
        }
    }

    override fun inverseMap(model: YUVImage): CameraImage {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}