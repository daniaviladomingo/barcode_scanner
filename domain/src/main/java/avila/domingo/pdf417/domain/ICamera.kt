package avila.domingo.pdf417.domain

import avila.domingo.pdf417.domain.model.Image
import io.reactivex.Observable

interface ICamera {
    fun images(): Observable<Image>
}