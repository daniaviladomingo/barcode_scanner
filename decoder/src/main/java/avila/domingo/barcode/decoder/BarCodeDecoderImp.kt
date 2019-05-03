package avila.domingo.barcode.decoder

import avila.domingo.barcode.decoder.mapper.ResultMapper
import avila.domingo.barcode.domain.IBarCodeDecoder
import avila.domingo.barcode.domain.model.Image
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import io.reactivex.Single

class BarCodeDecoderImp(
    private val reader: Reader,
    private val hints: Map<DecodeHintType, *>,
    private val mapper: ResultMapper
) : IBarCodeDecoder {
    override fun decode(image: Image): Single<String> = Single.create {
        try {
            val time = System.currentTimeMillis()
            println("decode")
            val result = mapper.map(
                reader.decode(
                    BinaryBitmap(
                        HybridBinarizer(RGBLuminanceSource(image.width, image.height, image.pixels))
                    ), hints
                )
            )
            println("time: ${System.currentTimeMillis() - time}")

            it.onSuccess(result)
        } catch (e: Exception) {
            e.printStackTrace()
            println(e.localizedMessage)
            it.onSuccess("")
        }
    }
}