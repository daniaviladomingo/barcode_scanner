package avila.domingo.barcode.domain.interactor.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

abstract class BaseUseCase<IN, OUT>(
    private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(param: IN): Result<OUT> = withContext(dispatcher) {
        try {
            Result.success(block(param))
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    abstract suspend fun block(param: IN): OUT
}