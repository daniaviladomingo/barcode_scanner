@file:Suppress("DEPRECATION")

package avila.domingo.barcode.camera

import avila.domingo.barcode.domain.ICamera
import avila.domingo.barcode.domain.model.PreviewImage
import io.reactivex.Single

class CameraImp(
    private val nativeCamera: INativeCamera,
    private val cameraRotationUtil: CameraRotationUtil
) : ICamera {
    override fun getImage(): Single<PreviewImage> =
        Single.create<PreviewImage> {
            try {
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
            } catch (e: RuntimeException) {
            }
        }
}