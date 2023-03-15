package avila.domingo.barcode.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import avila.domingo.barcode.camera.model.mapper.IllegalCameraAccess
import avila.domingo.barcode.domain.interactor.BarCodeReaderUseCase
import avila.domingo.barcode.ui.data.Resource
import avila.domingo.barcode.util.SingleLiveEvent
import com.google.zxing.ChecksumException
import com.google.zxing.FormatException
import com.google.zxing.NotFoundException
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val barCodeReaderUseCase: BarCodeReaderUseCase,
) : ViewModel() {

    val barcodeLiveData = SingleLiveEvent<Resource<String>>()

    fun read() {
        viewModelScope.launch {
            barCodeReaderUseCase(Unit).collect { result ->
                    result.onSuccess { info ->
                            barcodeLiveData.value = Resource.success(info)
                        }.onFailure { error ->
                            when (error) {
                                is NotFoundException -> {}
                                is ChecksumException -> {}
                                is FormatException -> {}
                                is IllegalCameraAccess -> {}
                                else -> {
                                    barcodeLiveData.value =
                                        Resource.error(error.localizedMessage ?: "Error")
                                }
                            }
                        }
                }
        }
    }
}