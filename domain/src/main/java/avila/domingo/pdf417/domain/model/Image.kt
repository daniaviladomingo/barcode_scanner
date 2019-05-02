package avila.domingo.pdf417.domain.model

data class Image(
    val image: ByteArray,
    val width: Int,
    val height: Int,
    val degrees: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Image

        if (!image.contentEquals(other.image)) return false
        if (degrees != other.degrees) return false

        return true
    }

    override fun hashCode(): Int {
        var result = image.contentHashCode()
        result = 31 * result + degrees
        return result
    }
}