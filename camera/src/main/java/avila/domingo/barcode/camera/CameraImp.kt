@file:Suppress("DEPRECATION")

package avila.domingo.barcode.camera

import avila.domingo.barcode.camera.model.mapper.IllegalCameraAccess
import avila.domingo.barcode.domain.ICamera
import avila.domingo.barcode.domain.model.PreviewImage
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CameraImp(
    private val nativeCamera: INativeCamera,
    private val cameraRotationUtil: CameraRotationUtil
) : ICamera {
    override suspend fun getImage(): Result<PreviewImage> = suspendCoroutine { continuation ->
        try {
            nativeCamera.camera().autoFocus { b, camera ->
                if (b) {
                    nativeCamera.camera().setOneShotPreviewCallback { data, _ ->
                        val previewSize = camera.parameters.previewSize
                        continuation.resume(
                            Result.success(
                                PreviewImage(
                                    data,
                                    previewSize.width,
                                    previewSize.height,
                                    cameraRotationUtil.rotationDegrees()
                                )
                            )
                        )
                    }
                }
            }
        } catch (e: IllegalCameraAccess) {
            continuation.resume(Result.failure(e))
        }
    }
}
