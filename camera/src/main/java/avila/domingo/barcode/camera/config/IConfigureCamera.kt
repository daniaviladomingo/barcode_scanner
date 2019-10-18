@file:Suppress("DEPRECATION")

package avila.domingo.barcode.camera.config

import android.hardware.Camera
import avila.domingo.barcode.domain.model.CameraSide

interface IConfigureCamera {
    fun configure(camera: Camera?, cameraSide: CameraSide)
}