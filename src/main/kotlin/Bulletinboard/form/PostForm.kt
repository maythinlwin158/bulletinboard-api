package Bulletinboard.form

import com.opencsv.bean.CsvBindByPosition
import javax.validation.constraints.NotNull

class PostForm {
    @CsvBindByPosition(position = 0)
    @NotNull(message = "The title is required.")
    val title: String? = null

    @CsvBindByPosition(position = 1)
    @NotNull(message = "The description is required.")
    val description: String? = null

    val status: Boolean = true
}