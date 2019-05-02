package avila.domingo.pdf417.camera.model

data class CameraImage(
    val image: ByteArray,
    val imageFormat: Int,
    val width: Int,
    val height: Int,
    val rotationDegrees: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CameraImage

        if (imageFormat != other.imageFormat) return false
        if (!image.contentEquals(other.image)) return false
        if (width != other.width) return false
        if (height != other.height) return false
        if (rotationDegrees != other.rotationDegrees) return false

        return true
    }

    override fun hashCode(): Int {
        var result = imageFormat.hashCode()
        result = 31 * result + image.contentHashCode()
        result = 31 * result + width
        result = 31 * result + height
        result = 31 * result + rotationDegrees
        return result
    }

}