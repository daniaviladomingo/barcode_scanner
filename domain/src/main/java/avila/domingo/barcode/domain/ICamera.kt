package avila.domingo.barcode.domain

import avila.domingo.barcode.domain.model.Image
import io.reactivex.Single

interface ICamera {
    fun getImage(): Single<Image>
}