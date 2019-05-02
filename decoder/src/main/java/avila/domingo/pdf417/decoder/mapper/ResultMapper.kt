package avila.domingo.pdf417.decoder.mapper

import avila.domingo.pdf417.domain.model.mapper.Mapper
import com.google.zxing.Result


class ResultMapper: Mapper<Result, String>() {
    override fun map(model: Result): String = model.text

    override fun inverseMap(model: String): Result {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}