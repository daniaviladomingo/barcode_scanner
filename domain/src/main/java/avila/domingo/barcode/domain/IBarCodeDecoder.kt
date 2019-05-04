package avila.domingo.barcode.domain

import avila.domingo.barcode.domain.model.YUVImage
import io.reactivex.Single

interface IBarCodeDecoder {
    fun decode(image: YUVImage): Single<String>
}