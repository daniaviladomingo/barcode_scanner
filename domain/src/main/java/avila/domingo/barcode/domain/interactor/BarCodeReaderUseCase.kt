package avila.domingo.barcode.domain.interactor

import avila.domingo.barcode.domain.IBarCodeManager
import avila.domingo.barcode.domain.interactor.type.ObservableUseCase
import io.reactivex.Observable

class BarCodeReaderUseCase(private val reader: IBarCodeManager) : ObservableUseCase<String> {
    override fun execute(): Observable<String> = reader.read()
}