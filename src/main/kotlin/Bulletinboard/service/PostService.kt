package Bulletinboard.service

import Bulletinboard.DTO.PostEditDTO
import Bulletinboard.model.Posts
import Bulletinboard.repository.PostRepository
import Bulletinboard.DTO.PostListDTO
import Bulletinboard.form.PostEditForm
import Bulletinboard.form.PostForm
import Bulletinboard.repository.UserRepository
import org.springframework.data.domain.Pageable
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class PostService(private val postRepository: PostRepository, val userRepository: UserRepository) {

    /**
     * Get All Posts By Pagination
     *
     * @param search String
     * @param pageable Pageable
     * @return List<PostListDTO> PostListDTO
     */
    fun getAllPosts(search: String, pageable: Pageable): List<PostListDTO> = postRepository.findALlPost(search, pageable)

    /**
     * Get All Posts
     *
     * @param search String
     * @return List<PostListDTO> PostListDTO
     */
    fun getAllPostsForExcel(search: String): List<PostListDTO> = postRepository.findALlPostForExcel(search)

    /**
     * Add Post into post table
     *
     * @param postForm PostForm
     * @return true/false Boolean
     */
    fun createPost(postForm: PostForm): Boolean {

        val auth = SecurityContextHolder.getContext().authentication
        val authUser = userRepository.findByName(auth.name)
        val authId = authUser.get().id

          var post: Posts = Posts()
          post.title = postForm.title
          post.description = postForm.description
          post.createdUserId = authId
          post.updatedUserId = authId
        return try {
            postRepository.save(post)
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Find Post By Title
     *
     * @param title String
     * @return Posts
     */
    fun findByTitle(title: String): Boolean { return postRepository.findByTitle(title) == null }

    /**
     * Get Post By Id
     *
     * @param postId Integer
     * @return PostEditDTO
     */
    fun getPostById(postId: Int): Optional<PostEditDTO> = postRepository.getPostById(postId)

    /**
     * Get Post By Title And Id
     *
     * @param postId Integer
     * @param title String
     * @return Boolean
     */
    fun findByTitleAndId(postId: Int, title: String): Boolean { return postRepository.findByTitleAndId(postId, title).isPresent }

    /**
     * Update Post By Id
     *
     * @param postId Integer
     * @param post PostEditForm
     * @return true/false Boolean
     */
    fun updatePostById(postId: Int, post: PostEditForm): Boolean {
        val auth = SecurityContextHolder.getContext().authentication
        val authUser = userRepository.findByName(auth.name)
        val authId = authUser.get().id

        return postRepository.findById(postId).map { oldPost ->
                val newPost: Posts =
                    oldPost.copy(title = post.title, description = post.description, status = post.status, updatedUserId = authId)
                try {
                    postRepository.save(newPost)
                    true
                } catch (e: Exception) {
                    false
                }
            }.orElse(false)
    }

    /**
     * Delete Post By Id
     *
     * @param postId Integer
     * @return true/false Boolean
     */
    fun postDeleteById(postId: Int): Boolean {
        return postRepository.findById(postId).map { oldPost ->
            val newPost = oldPost.copy(deletedAt = LocalDateTime.now(), deletedUserId = 1)
            try {
                postRepository.save(newPost)
                true
            } catch (e: Exception) {
                false
            }
        }.orElse(false)
    }
}