package avila.domingo.barcode.decoder.mapper

import avila.domingo.barcode.domain.model.YUVImage
import avila.domingo.barcode.domain.model.mapper.Mapper
import com.google.zxing.BinaryBitmap
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.common.HybridBinarizer

class BinaryBitmapMapper : Mapper<YUVImage, BinaryBitmap>() {
    override fun map(model: YUVImage): BinaryBitmap = model.run {
        BinaryBitmap(HybridBinarizer(PlanarYUVLuminanceSource(data, width, height, 0, 0, width, height, false)))
    }

    override fun inverseMap(model: BinaryBitmap): YUVImage {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}