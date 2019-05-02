package avila.domingo.pdf417.manager

import avila.domingo.pdf417.domain.IBarCodeDecoder
import avila.domingo.pdf417.domain.ICamera
import avila.domingo.pdf417.domain.IBarCodeManager
import io.reactivex.Observable

class BarCodeManagerImp(
    private val camera: ICamera,
    private val decoder: IBarCodeDecoder
): IBarCodeManager {
    override fun read(): Observable<String> = camera.images().flatMap { decoder.decode(it).toObservable() }
}