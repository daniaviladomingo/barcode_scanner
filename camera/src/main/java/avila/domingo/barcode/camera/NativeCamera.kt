@file:Suppress("DEPRECATION")

package avila.domingo.barcode.camera

import android.hardware.Camera
import avila.domingo.barcode.domain.model.CameraSide
import avila.domingo.barcode.domain.model.mapper.Mapper

class NativeCamera(
    private val configureCamera: IConfigureCamera,
    private val cameraSideMapper: Mapper<CameraSide, Int>
) {
    private var currentCamera: Camera? = null
    private var currentCameraSide: CameraSide? = null

    fun getCamera(cameraSide: CameraSide): Camera? {
        currentCamera?.run {
            if (cameraSide != currentCameraSide) {
                currentCamera?.release()
                openCamera(cameraSide)
            }
        } ?: openCamera(cameraSide)

        return currentCamera
    }

    fun getCurrentCamera(): Camera? = currentCamera

    private fun openCamera(cameraSide: CameraSide) {
        try {
            currentCamera = Camera.open(cameraSideMapper.map(cameraSide))
            configureCamera.configure(currentCamera, cameraSide)
            currentCameraSide = cameraSide
        } catch (e: Exception) {
        }
    }
}