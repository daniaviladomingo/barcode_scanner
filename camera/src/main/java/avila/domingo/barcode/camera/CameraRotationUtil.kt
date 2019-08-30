@file:Suppress("DEPRECATION")

package avila.domingo.barcode.camera

import android.hardware.Camera
import android.util.Log
import android.view.Surface
import android.view.WindowManager
import avila.domingo.barcode.domain.model.CameraSide
import avila.domingo.barcode.domain.model.mapper.Mapper

class CameraRotationUtil(
    private val windowManager: WindowManager,
    private val cameraSideMapper: Mapper<CameraSide, Int>
) {
    fun rotationDegrees(cameraSide: CameraSide): Int {
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

    fun rotationDegreesSurface(): Int = when (windowManager.defaultDisplay.rotation) {
        Surface.ROTATION_0 -> 90
        Surface.ROTATION_90 -> 0
        Surface.ROTATION_180 -> 90
        Surface.ROTATION_270 -> 180
        else -> 0
    }.apply {
        Log.d("xxxx", "${windowManager.defaultDisplay.rotation}")
    }
}