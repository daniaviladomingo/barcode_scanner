@file:Suppress("DEPRECATION")

package avila.domingo.barcode.camera

import android.hardware.Camera
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import avila.domingo.barcode.domain.ICamera
import avila.domingo.barcode.domain.model.CameraSide
import avila.domingo.barcode.domain.model.PreviewImage
import io.reactivex.Single

class CameraImp(
    nativeCamera: NativeCamera,
    private val cameraRotationUtil: CameraRotationUtil,
    private val surfaceView: SurfaceView,
    initialCameraSide: CameraSide,
    lifecycle: Lifecycle // Esto es simplemente para start/stop la preview cuando la activity sale/entre en segundo plano
) : ICamera, LifecycleObserver {

    private val tag = this::class.java.name

    private var currentCameraSide = initialCameraSide
    private var currentCamera: Camera? = null

    init {
        lifecycle.addObserver(this)

        surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
                Log.d(tag, "CALLBACK: surfaceChanged")
//                currentCamera?.startPreview()
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                Log.d(tag, "CALLBACK: surfaceDestroyed")
//                currentCamera?.stopPreview()
            }

            override fun surfaceCreated(holder: SurfaceHolder) {
                Log.d(tag, "CALLBACK: surfaceCreated")
                currentCamera?.setPreviewDisplay(holder)
            }
        })

        currentCamera = nativeCamera.getCamera(initialCameraSide)
    }

    override fun getImage(): Single<PreviewImage> =
        Single.create<PreviewImage> {
            currentCamera?.autoFocus { b, camera ->
                if (b) {
                    camera.setOneShotPreviewCallback { data, _ ->
                        val previewSize = camera.parameters.previewSize
                        it.onSuccess(
                            PreviewImage(
                                data,
                                previewSize.width,
                                previewSize.height,
                                cameraRotationUtil.rotationDegrees(currentCameraSide)
                            )
                        )
                    }
                }
            }
        }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        Log.d(tag, "start()")
        currentCamera?.setPreviewDisplay(surfaceView.holder)
        currentCamera?.startPreview()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        Log.d(tag, "stop()")
        currentCamera?.stopPreview()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destroy() {
        Log.d(tag, "destroy()")
        currentCamera?.stopPreview()
        currentCamera?.release()
        currentCamera = null
    }
}