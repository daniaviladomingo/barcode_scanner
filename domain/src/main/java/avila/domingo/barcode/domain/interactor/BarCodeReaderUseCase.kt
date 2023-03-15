package avila.domingo.barcode.domain.interactor

import avila.domingo.barcode.domain.IBarCodeDecoder
import avila.domingo.barcode.domain.ICamera
import avila.domingo.barcode.domain.interactor.base.BaseUseCaseFlow
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BarCodeReaderUseCase(
    private val barCodeDecoder: IBarCodeDecoder,
    private val camera: ICamera,
    dispatcher: CoroutineDispatcher,
) : BaseUseCaseFlow<Unit, String>(dispatcher) {
    override fun build(param: Unit): Flow<Result<String>> = flow {
        while (true) {
            val image = camera.getImage()
            image
                .onSuccess { emit(barCodeDecoder.decode(it)) }
                .onFailure { emit(Result.failure(it)) }
            delay(2000)
        }
    }
}