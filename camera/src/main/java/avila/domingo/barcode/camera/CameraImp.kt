@file:Suppress("DEPRECATION")

package avila.domingo.barcode.camera

import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import avila.domingo.barcode.domain.ICamera
import avila.domingo.barcode.domain.model.PreviewImage
import io.reactivex.Single

class CameraImp(
    private val nativeCamera: INativeCamera,
    private val cameraRotationUtil: CameraRotationUtil,
    private val surfaceView: SurfaceView,
    lifecycle: () -> Lifecycle // Esto es simplemente para start/stop la preview cuando la activity sale/entre en segundo plano
) : ICamera, LifecycleObserver {

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
            nativeCamera.camera().setPreviewDisplay(holder)
        }
    }

    init {
        lifecycle.invoke().addObserver(this)
        surfaceView.holder.addCallback(surfaceHolderCallback)
    }

    override fun getImage(): Single<PreviewImage> =
        Single.create<PreviewImage> {
            nativeCamera.camera().autoFocus { b, camera ->
                if (b) {
                    nativeCamera.camera().setOneShotPreviewCallback { data, _ ->
                        val previewSize = camera.parameters.previewSize
                        it.onSuccess(
                            PreviewImage(
                                data,
                                previewSize.width,
                                previewSize.height,
                                cameraRotationUtil.rotationDegrees()
                            )
                        )
                    }
                }
            }
        }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        nativeCamera.camera().setPreviewDisplay(surfaceView.holder)
        nativeCamera.camera().startPreview()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        nativeCamera.camera().stopPreview()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destroy() {
        surfaceView.holder.removeCallback(surfaceHolderCallback)
        nativeCamera.camera().stopPreview()
        nativeCamera.camera().release()
    }
}