package avila.domingo.pdf417.domain.interactor.type

import io.reactivex.Completable

interface CompletableUseCase {
    fun execute(): Completable
}