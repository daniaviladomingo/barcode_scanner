package avila.domingo.barcode.domain

import avila.domingo.barcode.domain.model.Image
import io.reactivex.Observable

interface ICamera {
    fun images(): Observable<Image>
}