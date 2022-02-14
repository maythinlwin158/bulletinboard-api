package Bulletinboard.form

import javax.validation.constraints.NotNull

class PasswordForm {

    @NotNull(message = "The password is required.")
    val password: String? = null

    @NotNull(message = "The new password is required.")
    val newPassword: String? = null
}