package avila.domingo.barcode.domain.interactor.type

import io.reactivex.Observable

interface UseCase<P> {
    fun execute(): Observable<P>
}