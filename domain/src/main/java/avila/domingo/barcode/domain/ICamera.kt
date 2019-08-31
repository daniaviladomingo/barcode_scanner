package avila.domingo.barcode.domain

import avila.domingo.barcode.domain.model.PreviewImage
import io.reactivex.Single

interface ICamera {
    fun getImage(): Single<PreviewImage>
}