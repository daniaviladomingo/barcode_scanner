package avila.domingo.barcode.domain

import avila.domingo.barcode.domain.model.PreviewImage

interface ICamera {
    suspend fun getImage(): Result<PreviewImage>
}