package Bulletinboard.repository

import Bulletinboard.DTO.UserListDTO
import Bulletinboard.model.Users
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository: JpaRepository<Users, Int> {

    /**
     * Find All Users
     *
     * @param name String
     * @param email String
     * @param pageable Pageable
     * @return List<UserListDTO>
     */
    @Query("SELECT u.name as name, u.user.name as createdUserName, u.email as email, u.phone as phone, u.profile as profile, u.dob as dob, u.address as address, u.createdAt as createdAt, u.updatedAt as updatedAt FROM Users u WHERE u.name like %?1% AND u.email like %?2% ORDER BY createdAt desc")
    fun findAllUsers(name: String, email: String, pageable: Pageable): List<UserListDTO>

    /**
     * Get User By Id
     *
     * @param userId Integer
     * @return UserListDTO
     */
    @Query("SELECT u.name as name, u.email as email,u.profile as profile, u.type as type, u.phone as phone, u.dob as dob, u.address as address FROM Users u WHERE u.deletedAt is null AND u.id = ?1")
    fun userById(userId: Int): Optional<UserListDTO>

    /**
     * Find User By Email
     *
     * @param email String
     * @return Users
     */
    fun findByEmail(email:String): Users?
}