@file:Suppress("DEPRECATION")

package avila.domingo.barcode.camera

import android.graphics.Point
import android.hardware.Camera
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.WindowManager
import avila.domingo.barcode.camera.model.CameraImage
import avila.domingo.barcode.camera.model.mapper.AllImageMapper
import avila.domingo.barcode.domain.ICamera
import avila.domingo.barcode.domain.model.Image
import io.reactivex.Observable
import java.lang.Math.abs
import java.util.*
import java.util.concurrent.TimeUnit

class CameraImp(
    private val windowManager: WindowManager,
    private val period: Long,
    private val timeUnit: TimeUnit,
    private val cameraMapper: AllImageMapper,
    surfaceView: SurfaceView
) : ICamera {
    private var camera: Camera? = null
    private val cameraInfo = Camera.CameraInfo()

    private val screenSize = screenSize()

    private var cameraTimer: Timer? = null

    init {
        surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                initCamera(holder)
            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
                configCamera()
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                releaseCamera()
            }
        })
    }

    private fun initCamera(holder: SurfaceHolder) {
        try {
            camera = Camera.open(0)
            camera?.setPreviewDisplay(holder)
        } catch (e: Exception) {
        }
    }

    private fun configCamera() {
        try {
            camera?.run {
                val customParameters = parameters

                val screenRatio = screenSize.witdh / screenSize.height.toFloat()

                var diff = Float.MAX_VALUE
                var previewWidth = 0
                var previewHeight = 0

                customParameters.supportedPreviewSizes
                    .sortedByDescending { it.width }
                    .apply {
                        this.forEach {
                            val previewDiff = abs((it.width / it.height.toFloat()) - screenRatio)
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
                startPreview()
            }
        } catch (e: Exception) {
        }
    }

    private fun releaseCamera() {
        cameraTimer?.cancel()
        cameraTimer?.purge()
        cameraTimer = null
        camera?.stopPreview()
        camera?.release()
        camera = null
    }

    private fun screenSize(): Size =
        Point().apply { windowManager.defaultDisplay.getSize(this) }.let { point ->
            if (point.x > point.y) {
                Size(point.x, point.y)
            } else {
                Size(point.y, point.x)
            }
        }


    private fun rotationDegrees(): Int {
        var rotationDegrees = when (windowManager.defaultDisplay.rotation) {
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            Surface.ROTATION_0 -> 0
            else -> 0
        }

        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
            rotationDegrees = 360 - rotationDegrees
        }

        return (cameraInfo.orientation + rotationDegrees) % 360
    }

    override fun images(): Observable<Image> = Observable.interval(period, timeUnit).flatMap {
        Observable.create<Image> {
            camera?.autoFocus { b, camera ->
                if (b) {
                    camera.setOneShotPreviewCallback { data, _ ->
                        val previewSize = camera.parameters.previewSize
                        it.onNext(
                            cameraMapper.map(
                                CameraImage(
                                    data,
                                    camera.parameters.previewFormat,
                                    previewSize.width,
                                    previewSize.height,
                                    rotationDegrees()
                                )
                            )
                        )
                    }
                }
            }
        }
    }

    internal data class Size(val witdh: Int, val height: Int)
}