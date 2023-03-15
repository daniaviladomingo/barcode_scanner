package avila.domingo.barcode.base.mvi

sealed interface LoadingUiState {
    object Loading : LoadingUiState
    object NotLoading : LoadingUiState
}

sealed interface BasicUiState<out T> {
    data class Data<out T>(val data: T) : BasicUiState<T>
    object Idle : BasicUiState<Nothing>
}

fun <T> BasicUiState<T>.handle(
    onData: ((data: T) -> Unit),
) {
    when (this) {
        is BasicUiState.Idle -> Unit
        is BasicUiState.Data -> onData(data)
    }
}
