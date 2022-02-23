package Bulletinboard.DTO

data class UserDto (
    var username: String,
    var password: String,
    var email: String? = null,
    var authorities: Set<String>? = null,
    var createdBy: String? = null,
    var lastModifiedBy: String? = null
)