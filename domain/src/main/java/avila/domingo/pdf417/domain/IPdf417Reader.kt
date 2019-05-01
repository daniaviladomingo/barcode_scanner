package avila.domingo.pdf417.domain

import avila.domingo.pdf417.domain.model.Image
import io.reactivex.Single

interface IPdf417Reader {
    fun decode(image: Image): Single<String>
}