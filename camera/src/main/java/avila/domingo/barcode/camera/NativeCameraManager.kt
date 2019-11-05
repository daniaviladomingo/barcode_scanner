@file:Suppress("DEPRECATION")

package avila.domingo.barcode.camera

import android.graphics.Point
import android.hardware.Camera
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.WindowManager
import avila.domingo.barcode.lifecycle.ILifecycleObserver
import avila.domingo.barcode.camera.model.mapper.CameraSideMapper
import avila.domingo.barcode.domain.model.CameraSide
import kotlin.math.abs

class NativeCameraManager(
    private val cameraSideMapper: CameraSideMapper,
    private val windowManager: WindowManager,
    private val rangePreview: IntRange,
    private val surfaceView: SurfaceView,
    private val cameraSide: CameraSide
) : INativeCamera, ILifecycleObserver {

    private lateinit var currentCamera: Camera

    private val surfaceHolderCallback = object : SurfaceHolder.Callback {
        override fun surfaceChanged(
            holder: SurfaceHolder,
            format: Int,
            width: Int,
            height: Int
        ) {
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {}

        override fun surfaceCreated(holder: SurfaceHolder) {
            currentCamera.setPreviewDisplay(holder)
        }
    }

    override fun camera(): Camera = currentCamera

    private fun openCamera(cameraSide: CameraSide) {
        currentCamera = Camera.open(cameraSideMapper.map(cameraSide))
        configure()
    }

    private fun configure() {
        currentCamera.run {

            val screenSize = screenSize()

            val customParameters = parameters

            val screenRatio = screenSize.witdh / screenSize.height.toFloat()

            var diff = Float.MAX_VALUE
            var previewWidth = 0
            var previewHeight = 0

            customParameters.supportedPreviewSizes
                .filter {
                    it.width in rangePreview
                }
                .sortedByDescending { it.width }
                .apply {
                    this.forEach {
                        val previewDiff =
                            abs((it.width / it.height.toFloat()) - screenRatio)
                        if (previewDiff < diff) {
                            diff = previewDiff
                            previewWidth = it.width
                            previewHeight = it.height
                        }
                    }
                }
                .filter { screenRatio == (it.width / it.height.toFloat()) }
                .run {
                    if (size > 0) {
                        get(0).let { customParameters.setPreviewSize(it.width, it.height) }
                    } else {
                        customParameters.setPreviewSize(previewWidth, previewHeight)
                    }
                }

            parameters = customParameters

            setDisplayOrientation(getCameraDisplayOrientation())
        }
    }

    private fun getCameraDisplayOrientation(): Int {
        val info = Camera.CameraInfo()
        Camera.getCameraInfo(cameraSideMapper.map(cameraSide), info)

        val degrees = when (windowManager.defaultDisplay.rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> 0
        }

        return if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            (360 - (info.orientation + degrees) % 360) % 360  // compensate the mirror
        } else {  // back-facing
            (info.orientation - degrees + 360) % 360
        }
    }

    private fun screenSize(): Size =
        Point().apply { windowManager.defaultDisplay.getSize(this) }.let { point ->
            if (point.x > point.y) {
                Size(point.x, point.y)
            } else {
                Size(point.y, point.x)
            }
        }

    internal data class Size(val witdh: Int, val height: Int)

    override fun start() {
        surfaceView.holder.addCallback(surfaceHolderCallback)
        openCamera(cameraSide)
        currentCamera.setPreviewDisplay(surfaceView.holder)
        currentCamera.startPreview()
    }

    override fun stop() {
        currentCamera.stopPreview()
    }


    override fun destroy() {
        surfaceView.holder.removeCallback(surfaceHolderCallback)
        currentCamera.cancelAutoFocus()
        currentCamera.stopPreview()
        currentCamera.release()
    }
}