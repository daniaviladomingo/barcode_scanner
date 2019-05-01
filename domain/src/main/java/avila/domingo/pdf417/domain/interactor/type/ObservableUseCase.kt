package avila.domingo.pdf417.domain.interactor.type

import io.reactivex.Observable

interface ObservableUseCase<P> {
    fun execute(): Observable<P>
}