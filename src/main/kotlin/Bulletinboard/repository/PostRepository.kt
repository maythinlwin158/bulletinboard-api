package Bulletinboard.repository

import Bulletinboard.DTO.PostListDTO
import Bulletinboard.DTO.PostEditDTO
import Bulletinboard.model.Posts
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
public interface PostRepository: CrudRepository<Posts, Int> {

    /**
     * Find ALl Post By Pagination
     *
     * @param search String
     * @param pageable Pageable
     * @return List<PostListDTO> PostListDTO
     */
    @Query("SELECT p.user.name as createdUsername, p.updatedUser.name as updatedUsername, p.title as title, p.description as description, p.status as status, p.createdAt as createdAt, p.updatedAt as updatedAt FROM Posts p WHERE p.deletedAt is null AND (p.title like %?1% or p.description like %?1% or p.user.name like %?1%) ORDER BY createdAt desc")
    fun findALlPost(search: String, pageable: Pageable): List<PostListDTO>


    /**
     * Find ALl Post
     *
     * @param search String
     * @return List<PostListDTO> PostListDTO
     */
    @Query("SELECT p.user.name as createdUsername, p.updatedUser.name as updatedUsername, p.title as title, p.description as description, p.status as status, p.createdAt as createdAt, p.updatedAt as updatedAt FROM Posts p WHERE p.deletedAt is null AND (p.title like %?1% or p.description like %?1% or p.user.name like %?1%) ORDER BY createdAt desc")
    fun findALlPostForExcel(search: String): List<PostListDTO>

    /**
     * Get Post By Id
     *
     * @param postId Integer
     * @return PostEditDTO
     */
    @Query("SELECT p.title as title, p.description as description, p.status as status, p.createdUserId as createdUserId, p.updatedUserId as updatedUserId FROM Posts p WHERE p.deletedAt is null AND p.id = ?1")
    fun getPostById(postId: Int): Optional<PostEditDTO>
}