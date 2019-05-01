package avila.domingo.pdf417.domain.interactor.type

import io.reactivex.Observable

interface UseCase<P> {
    fun execute(): Observable<P>
}