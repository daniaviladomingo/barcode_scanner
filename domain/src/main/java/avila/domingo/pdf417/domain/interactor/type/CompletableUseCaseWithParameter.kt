package avila.domingo.pdf417.domain.interactor.type

import io.reactivex.Completable

interface CompletableUseCaseWithParameter<P> {
    fun execute(parameter: P): Completable
}