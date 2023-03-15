package avila.domingo.barcode.domain

import avila.domingo.barcode.domain.model.PreviewImage

interface IBarCodeDecoder {
    suspend fun decode(image: PreviewImage): Result<String>
}