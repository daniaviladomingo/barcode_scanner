package avila.domingo.barcode.screen

import android.graphics.Point
import android.view.Surface
import android.view.WindowManager
import avila.domingo.barcode.domain.IScreen
import avila.domingo.barcode.domain.model.Size

class ScreenImp(
    private val windowManager: WindowManager
) : IScreen {
    override fun getSize(): Size = Point().apply { windowManager.defaultDisplay.getSize(this) }.let { point ->
        if (point.x > point.y) {
            Size(point.x, point.y)
        } else {
            Size(point.y, point.x)
        }
    }

    override fun getRotationDegrees(): Int = when (windowManager.defaultDisplay.rotation) {
        Surface.ROTATION_90 -> 90
        Surface.ROTATION_180 -> 180
        Surface.ROTATION_270 -> 270
        Surface.ROTATION_0 -> 0
        else -> 0
    }
}