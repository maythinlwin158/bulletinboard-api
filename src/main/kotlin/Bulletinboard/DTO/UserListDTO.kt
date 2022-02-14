package Bulletinboard.DTO

import java.util.*

interface UserListDTO {
    val name: String
    val createdUserName: String
    val email: String
    val phone: String
    val profile: String
    val address: String
    val dob: Date
    val createdAt: Date
    val updatedAt: Date
}