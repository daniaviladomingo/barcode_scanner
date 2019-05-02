package avila.domingo.barcode.domain

import io.reactivex.Observable

interface IBarCodeManager {
    fun read(): Observable<String>
}