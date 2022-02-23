package Bulletinboard.form

import java.io.File
import java.util.*
import javax.validation.constraints.NotNull

class UserForm {

    @field:NotNull(message = "The name is required.")
    val name: String? = null
//
//    @field:NotNull(message = "The name is required.")
//    val username: String? = null

    @field:NotNull(message = "The email is required.")
    val email: String? = null

    @field:NotNull(message = "The password is required.")
    var password: String? = null

    @field:NotNull(message = "Type is required")
    val type: Boolean = true

    var authorities: Set<String>? = null

    @field:NotNull(message = "Ph no is required")
    val phone: String? = null

    val address: String? = null

    val dob: Date? = null
}