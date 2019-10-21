@file:Suppress("DEPRECATION")

package avila.domingo.barcode.camera

import android.hardware.Camera
import android.view.Surface
import android.view.WindowManager
import avila.domingo.barcode.camera.model.mapper.CameraSideMapper
import avila.domingo.barcode.domain.model.CameraSide

class CameraRotationUtil(
    private val windowManager: WindowManager,
    private val cameraSide: CameraSide,
    private val cameraSideMapper: CameraSideMapper
) {
    fun rotationDegrees(): Int {
        val cameraInfo = Camera.CameraInfo()
        Camera.getCameraInfo(cameraSideMapper.map(cameraSide), cameraInfo)

        var degrees = when (windowManager.defaultDisplay.rotation) {
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            Surface.ROTATION_0 -> 0
            else -> 0
        }

        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
            degrees = 360 - degrees
        }

        return (cameraInfo.orientation + degrees) % 360
    }
}