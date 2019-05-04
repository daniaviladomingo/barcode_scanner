package avila.domingo.barcode.manager

import avila.domingo.barcode.domain.IBarCodeDecoder
import avila.domingo.barcode.domain.IBarCodeManager
import avila.domingo.barcode.domain.ICamera
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class BarCodeManagerImp(
    private val camera: ICamera,
    private val decoder: IBarCodeDecoder,
    private val period: Long,
    private val timeUnit: TimeUnit
) : IBarCodeManager {
    override fun read(): Observable<String> = Observable.interval(period, timeUnit).flatMap {
        camera.getImage().toObservable().flatMap {
            decoder.decode(it).toObservable()
        }
    }
}