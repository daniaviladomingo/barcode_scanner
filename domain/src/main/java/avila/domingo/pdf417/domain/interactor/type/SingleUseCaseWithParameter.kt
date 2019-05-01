package avila.domingo.pdf417.domain.interactor.type

import io.reactivex.Single

interface SingleUseCaseWithParameter<P, R> {
    fun execute(parameter: P): Single<R>
}
