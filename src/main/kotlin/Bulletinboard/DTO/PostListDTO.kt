package Bulletinboard.DTO

import java.util.*

interface PostListDTO {
    val createdUsername: String
    val updatedUsername: String
    val title: String
    val description: String
    val status: Boolean
    val createdAt: Date
    val updatedAt: Date
}