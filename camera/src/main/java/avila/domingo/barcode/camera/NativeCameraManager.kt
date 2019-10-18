@file:Suppress("DEPRECATION")

package avila.domingo.barcode.camera

import android.hardware.Camera
import avila.domingo.barcode.camera.config.IConfigureCamera
import avila.domingo.barcode.camera.model.mapper.CameraSideMapper
import avila.domingo.barcode.domain.model.CameraSide

class NativeCameraManager(
    private val configureCamera: IConfigureCamera,
    private val cameraSideMapper: CameraSideMapper
) {
    private lateinit var currentCamera: Camera
    private lateinit var currentCameraSide: CameraSide
    private var isCameraOpen = false

    fun getCamera(cameraSide: CameraSide): Camera {
        if (isCameraOpen) {
            if (cameraSide != currentCameraSide) {
                currentCamera.release()
                openCamera(cameraSide)
            }
        } else {
            openCamera(cameraSide)
            isCameraOpen = true
        }

        return currentCamera
    }

    private fun openCamera(cameraSide: CameraSide) {
        currentCamera = Camera.open(cameraSideMapper.map(cameraSide))
        configureCamera.configure(currentCamera, cameraSide)
        currentCameraSide = cameraSide
    }
}