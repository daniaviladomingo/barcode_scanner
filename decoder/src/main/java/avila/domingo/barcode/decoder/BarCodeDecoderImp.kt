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
            it.onSuccess(
                mapper.map(
                    reader.decode(
                        BinaryBitmap(
                            HybridBinarizer(
                                RGBLuminanceSource(
                                    image.width,
                                    image.height,
                                    image.pixels
                                )
                            )
                        ), hints
                    )
                )
            )
        } catch (e: Exception) {
            it.onSuccess("")
        }
    }
}