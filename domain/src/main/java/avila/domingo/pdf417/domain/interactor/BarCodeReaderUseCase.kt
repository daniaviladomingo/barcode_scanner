package avila.domingo.pdf417.domain.interactor

import avila.domingo.pdf417.domain.IBarCodeManager
import avila.domingo.pdf417.domain.interactor.type.ObservableUseCase
import io.reactivex.Observable

class BarCodeReaderUseCase(private val reader: IBarCodeManager) : ObservableUseCase<String> {
    override fun execute(): Observable<String> = reader.read()
}