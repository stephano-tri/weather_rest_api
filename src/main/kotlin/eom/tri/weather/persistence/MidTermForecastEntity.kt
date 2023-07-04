package eom.tri.weather.persistence

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("mid_term_forecast")
data class MidTermForecastEntity(
    @Id
    val id: Long? = null,
    val regId: String? = null,
    val rnSt3Am: Int? = null,
    val rnSt3Pm: Int? = null,
    val rnSt4Am: Int? = null,
    val rnSt4Pm: Int? = null,
    val rnSt5Am: Int? = null,
    val rnSt5Pm: Int? = null,
    val rnSt6Am: Int? = null,
    val rnSt6Pm: Int? = null,
    val rnSt7Am: Int? = null,
    val rnSt7Pm: Int? = null,
    val rnSt8: Int? = null,
    val rnSt9: Int? = null,
    val rnSt10: Int? = null,
    val wf3Am: String? = null,
    val wf3Pm: String? = null,
    val wf4Am: String? = null,
    val wf4Pm: String? = null,
    val wf5Am: String? = null,
    val wf5Pm: String? = null,
    val wf6Am: String? = null,
    val wf6Pm: String? = null,
    val wf7Am: String? = null,
    val wf7Pm: String? = null,
    val wf8: String? = null,
    val wf9: String? = null,
    val wf10: String? = null,
    val refDate: String? = null
)
