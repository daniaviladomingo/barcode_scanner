package avila.domingo.pdf417.domain

import avila.domingo.pdf417.domain.model.Image
import io.reactivex.Observable
import io.reactivex.Single

interface IBarCodeDecoder {
    fun decode(image: Image): Single<String>
}