package Bulletinboard.form

import javax.validation.constraints.NotNull

class PostEditForm {

    @NotNull(message = "The title is required.")
    val title: String? = null

    @NotNull(message = "The description is required.")
    val description: String? = null

    val status: Boolean = true
}