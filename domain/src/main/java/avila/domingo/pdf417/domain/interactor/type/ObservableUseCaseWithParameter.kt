package avila.domingo.pdf417.domain.interactor.type

import io.reactivex.Observable

interface ObservableUseCaseWithParameter<P, R> {
    fun execute(parameter: P): Observable<R>
}
