package Bulletinboard.form

import javax.validation.constraints.NotNull

class PostEditForm {

    @field:NotNull(message = "The title is required.")
    val title: String? = null

    @field:NotNull(message = "The description is required.")
    val description: String? = null

    val status: Boolean = true
}