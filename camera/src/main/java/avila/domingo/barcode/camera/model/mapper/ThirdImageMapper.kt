package avila.domingo.barcode.camera.model.mapper

import android.graphics.BitmapFactory
import android.graphics.Rect
import android.graphics.YuvImage
import android.util.Log
import avila.domingo.barcode.camera.model.CameraImage
import avila.domingo.barcode.domain.model.YUVImage
import avila.domingo.barcode.domain.model.mapper.Mapper
import java.io.ByteArrayOutputStream

class ThirdImageMapper : Mapper<CameraImage, YUVImage>() {
    override fun map(model: CameraImage): YUVImage = model.run {
        val time = System.currentTimeMillis()
        val yuv = YuvImage(image, imageFormat, width, height, null)
//        val out = ByteArrayOutputStream()
//        val div = height / 3
//        yuv.compressToJpeg(Rect(0, div, width, div * 2), 100, out)
//        val bytes = out.toByteArray()
//        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
//        val pixels = IntArray(bitmap.width * bitmap.height)
//        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        Log.d("ttt", "Time: ${System.currentTimeMillis() - time}")
        YUVImage(yuv.yuvData, yuv.width, yuv.height)
    }

    override fun inverseMap(model: YUVImage): CameraImage {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}