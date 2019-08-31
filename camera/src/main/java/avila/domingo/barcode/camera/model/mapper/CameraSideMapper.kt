@file:Suppress("DEPRECATION")

package avila.domingo.barcode.camera.model.mapper

import android.hardware.Camera
import avila.domingo.barcode.domain.model.CameraSide
import avila.domingo.barcode.domain.model.mapper.Mapper

class CameraSideMapper: Mapper<CameraSide, Int>() {
    override fun map(model: CameraSide): Int = when(model){
        CameraSide.BACK -> Camera.CameraInfo.CAMERA_FACING_BACK
        CameraSide.FRONT -> Camera.CameraInfo.CAMERA_FACING_FRONT
    }

    override fun inverseMap(model: Int): CameraSide {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}