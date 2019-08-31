package avila.domingo.barcode.domain

import avila.domingo.barcode.domain.model.PreviewImage
import io.reactivex.Single

interface IBarCodeDecoder {
    fun decode(image: PreviewImage): Single<String>
}