package avila.domingo.barcode.domain.interactor.type

import io.reactivex.Observable

interface ObservableUseCaseWithParameter<P, R> {
    fun execute(parameter: P): Observable<R>
}
