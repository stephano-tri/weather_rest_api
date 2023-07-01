package eom.tri.weather.util

import org.springframework.stereotype.Component
import java.text.SimpleDateFormat
import java.util.*

@Component
class UtilFunction {

    fun epochMillisToDate(epochMillis: Long): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return dateFormat.format(Date(epochMillis))
    }

}
