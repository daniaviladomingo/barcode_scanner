package avila.domingo.barcode.decoder

import avila.domingo.barcode.decoder.mapper.BinaryBitmapMapper
import avila.domingo.barcode.decoder.mapper.ResultMapper
import avila.domingo.barcode.domain.IBarCodeDecoder
import avila.domingo.barcode.domain.model.PreviewImage
import com.google.zxing.DecodeHintType
import com.google.zxing.Reader
import com.google.zxing.ReaderException

class BarCodeDecoderImp(
    private val reader: Reader,
    private val hints: Map<DecodeHintType, *>,
    private val resultMapper: ResultMapper,
    private val binaryBitmapMapper: BinaryBitmapMapper
) : IBarCodeDecoder {
    override suspend fun decode(image: PreviewImage): Result<String> =
        try {
            Result.success(resultMapper.map(reader.decode(binaryBitmapMapper.map(image), hints)))
        } catch (e: ReaderException) {
            Result.failure(e)
        }
}