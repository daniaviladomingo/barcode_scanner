@file:Suppress("DEPRECATION")

package avila.domingo.barcode.camera

import android.graphics.YuvImage
import android.hardware.Camera
import android.view.SurfaceHolder
import android.view.SurfaceView
import avila.domingo.barcode.camera.model.CameraId
import avila.domingo.barcode.domain.ICamera
import avila.domingo.barcode.domain.IScreen
import avila.domingo.barcode.domain.model.YUVImage
import io.reactivex.Single

class CameraImp(
    private val cameraId: CameraId,
    private val screen: IScreen,
    surfaceView: SurfaceView
) : ICamera {

    private var camera: Camera? = null
    private val cameraInfo = Camera.CameraInfo()

    init {
        Camera.getCameraInfo(cameraId.id, cameraInfo)

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
            camera = Camera.open(cameraId.id)
            camera?.setPreviewDisplay(holder)
        } catch (e: Exception) {
        }
    }

    private fun configCamera() {
        try {
            camera?.run {
                val customParameters = parameters

                val screenRatio = screen.getSize().width / screen.getSize().height.toFloat()

                var diff = Float.MAX_VALUE
                var previewWidth = 0
                var previewHeight = 0

                customParameters.supportedPreviewSizes
                    .sortedByDescending { it.width }
                    .apply {
                        this.forEach {
                            val previewDiff = Math.abs((it.width / it.height.toFloat()) - screenRatio)
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
        camera?.stopPreview()
        camera?.release()
        camera = null
    }

    override fun getImage(): Single<YUVImage> =
        Single.create<YUVImage> {
            camera?.autoFocus { b, camera ->
                if (b) {
                    camera.setOneShotPreviewCallback { data, _ ->
                        val previewSize = camera.parameters.previewSize

                        it.onSuccess(
                            YuvImage(data, camera.parameters.previewFormat, previewSize.width, previewSize.height, null).run {
                                YUVImage(yuvData, width, height)
                            }
                        )
                    }
                }
            }
        }

    private fun getFrameOrientation(): Int {
        var rotation = screen.getRotationDegrees()
        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
            rotation = 360 - rotation
        }
        return (cameraInfo.orientation + rotation) % 360
    }

}