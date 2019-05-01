package avila.domingo.pdf417.domain.model

data class Image(
    val picture: ByteArray,
    val degrees: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Image

        if (!picture.contentEquals(other.picture)) return false
        if (degrees != other.degrees) return false

        return true
    }

    override fun hashCode(): Int {
        var result = picture.contentHashCode()
        result = 31 * result + degrees
        return result
    }
}