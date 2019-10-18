@file:Suppress("DEPRECATION")

package avila.domingo.barcode.camera

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
    nativeCameraManager: NativeCameraManager,
    private val cameraRotationUtil: CameraRotationUtil,
    private val surfaceView: SurfaceView,
    private val cameraSide: CameraSide,
    lifecycle: Lifecycle // Esto es simplemente para start/stop la preview cuando la activity sale/entre en segundo plano
) : ICamera, LifecycleObserver {

    private val camera = nativeCameraManager.getCamera(cameraSide)

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
            camera.setPreviewDisplay(holder)
        }
    }

    init {
        lifecycle.addObserver(this)
        surfaceView.holder.addCallback(surfaceHolderCallback)
    }

    override fun getImage(): Single<PreviewImage> =
        Single.create<PreviewImage> {
            camera.autoFocus { b, camera ->
                if (b) {
                    camera.setOneShotPreviewCallback { data, _ ->
                        val previewSize = camera.parameters.previewSize
                        it.onSuccess(
                            PreviewImage(
                                data,
                                previewSize.width,
                                previewSize.height,
                                cameraRotationUtil.rotationDegrees(cameraSide)
                            )
                        )
                    }
                }
            }
        }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        camera.setPreviewDisplay(surfaceView.holder)
        camera.startPreview()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        camera.stopPreview()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destroy() {
        surfaceView.holder.removeCallback(surfaceHolderCallback)
        camera.stopPreview()
        camera.release()
    }
}