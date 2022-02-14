package Bulletinboard.service

import Bulletinboard.DTO.PostEditDTO
import Bulletinboard.model.Posts
import Bulletinboard.repository.PostRepository
import Bulletinboard.DTO.PostListDTO
import Bulletinboard.form.PostForm
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class PostService(private val postRepository: PostRepository) {

    fun getAllPosts(search: String, pageable: Pageable): List<PostListDTO> = postRepository.findALlPost(search, pageable)

    fun createPost(postForm: PostForm): Boolean {
          var post: Posts = Posts()
          post.title = postForm.title
          post.description = postForm.description
          post.createdUserId = 1
          post.updatedUserId = 1
        return try {
            postRepository.save(post)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getPostById(postId: Int): Optional<PostEditDTO> = postRepository.getPostById(postId)

    fun updatePostById(postId: Int, post: Posts): Boolean = postRepository.findById(postId).map { oldPost ->
        val newPost: Posts = oldPost.copy(title = post.title, description = post.description, status = post.status)
        try {
            postRepository.save(newPost)
             true
        } catch (e: Exception) {
            false
        }
    }.orElse(false)

    fun postDeleteById(postId: Int): Boolean {
        return postRepository.findById(postId).map { oldPost ->
            val newPost = oldPost.copy(deletedAt = LocalDateTime.now(), deletedUserId = 1)
            postRepository.save(newPost)
             true
        }.orElse(false)
    }
}