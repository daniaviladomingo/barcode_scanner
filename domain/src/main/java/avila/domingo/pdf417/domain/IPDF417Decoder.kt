package avila.domingo.pdf417.domain

import avila.domingo.pdf417.domain.model.Image
import io.reactivex.Single

interface IPDF417Decoder {
    fun decode(image: Image): Single<String>
}