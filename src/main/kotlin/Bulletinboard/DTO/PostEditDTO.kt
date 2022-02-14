package Bulletinboard.DTO

interface PostEditDTO {
    val title: String
    val description: String
    val status: Boolean
    val createdUserId: Int
    val updatedUserId: Int
}