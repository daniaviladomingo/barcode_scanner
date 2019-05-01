package avila.domingo.pdf417.domain.interactor.type

import io.reactivex.Single

interface SingleUseCase<P> {
    fun execute(): Single<P>
}