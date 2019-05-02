package avila.domingo.pdf417.decoder

import avila.domingo.pdf417.domain.IBarCodeDecoder
import avila.domingo.pdf417.domain.model.Image
import avila.domingo.pdf417.domain.model.mapper.Mapper
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import io.reactivex.Single

class BarCodeDecoderImp(
    private val reader: Reader,
    private val hints: Map<DecodeHintType, *>,
    private val mapper: Mapper<Result, String>
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