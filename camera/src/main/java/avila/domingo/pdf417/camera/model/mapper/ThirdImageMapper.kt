package avila.domingo.pdf417.camera.model.mapper

import android.graphics.BitmapFactory
import android.graphics.Rect
import android.graphics.YuvImage
import avila.domingo.pdf417.camera.model.CameraImage
import avila.domingo.pdf417.domain.model.Image
import avila.domingo.pdf417.domain.model.mapper.Mapper
import java.io.ByteArrayOutputStream

class ThirdImageMapper(
    private val screenHeight: Int
) : Mapper<CameraImage, Image>() {
    override fun map(model: CameraImage): Image = model.run {
        val yuv = YuvImage(image, imageFormat, width, height, null)
        val out = ByteArrayOutputStream()
        yuv.compressToJpeg(Rect(0, 0, width, height), 100, out)
        val bytes = out.toByteArray()
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        val pixels = IntArray(bitmap.width * bitmap.height)
        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        Image(pixels, width, height)
    }

    override fun inverseMap(model: Image): CameraImage {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}