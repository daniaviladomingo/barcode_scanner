package avila.domingo.barcode.domain

import avila.domingo.barcode.domain.model.Size

interface IScreen {
    fun getSize(): Size
    fun getRotationDegrees(): Int
}