@file:Suppress("DEPRECATION")

package avila.domingo.barcode.camera

import android.hardware.Camera

interface INativeCamera {
    fun camera(): Camera
}