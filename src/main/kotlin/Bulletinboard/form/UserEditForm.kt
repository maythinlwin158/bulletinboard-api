package Bulletinboard.form

import java.util.*
import javax.validation.constraints.NotNull

class UserEditForm {
    @NotNull(message = "The name is required.")
    val name: String? = null

    @NotNull(message = "The email is required.")
    val email: String? = null

    @NotNull(message = "Type is required")
    val type: Boolean = true

    @NotNull(message = "Ph no is required")
    val phone: String? = null

    val address: String? = null

    val dob: Date? = null
}