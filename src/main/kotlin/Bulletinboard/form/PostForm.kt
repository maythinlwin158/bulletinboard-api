package Bulletinboard.form

import com.opencsv.bean.CsvBindByPosition
import org.hibernate.validator.constraints.UniqueElements
import javax.persistence.Column
import javax.persistence.UniqueConstraint
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

class PostForm (
    @CsvBindByPosition(position = 0)
    @field:NotNull(message = "The title is required.")
    val title: String? = null,

    @CsvBindByPosition(position = 1)
    @field:NotNull(message = "The description is required.")
    val description: String? = null,

    val status: Boolean = true
)