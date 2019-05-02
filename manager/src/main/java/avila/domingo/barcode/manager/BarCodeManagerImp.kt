package avila.domingo.barcode.manager

import avila.domingo.barcode.domain.IBarCodeDecoder
import avila.domingo.barcode.domain.ICamera
import avila.domingo.barcode.domain.IBarCodeManager
import io.reactivex.Observable

class BarCodeManagerImp(
    private val camera: ICamera,
    private val decoder: IBarCodeDecoder
): IBarCodeManager {
    override fun read(): Observable<String> = camera.images().flatMap { decoder.decode(it).toObservable() }
}