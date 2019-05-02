package avila.domingo.pdf417.domain

import io.reactivex.Observable

interface IBarCodeManager {
    fun read(): Observable<String>
}