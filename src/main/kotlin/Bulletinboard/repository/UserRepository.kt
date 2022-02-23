package Bulletinboard.repository

import Bulletinboard.DTO.UserListDTO
import Bulletinboard.model.User
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository: JpaRepository<User, Int> {

    /**
     * Find All Users
     *
     * @param name String
     * @param email String
     * @param pageable Pageable
     * @return List<UserListDTO>
     */
    @Query("SELECT u.name as name, u.createdUserId as createdUserName, u.email as email, u.phone as phone, u.profile as profile, u.dob as dob, u.address as address, u.createdAt as createdAt, u.updatedAt as updatedAt FROM User u WHERE u.name like %?1% AND u.email like %?2% ORDER BY createdAt desc")
    fun findAllUsers(name: String, email: String, pageable: Pageable): List<UserListDTO>

    /**
     * Get User By Id
     *
     * @param userId Integer
     * @return UserListDTO
     */
    @Query("SELECT u.name as name, u.email as email,u.profile as profile, u.type as type, u.phone as phone, u.dob as dob, u.address as address FROM User u WHERE u.deletedAt is null AND u.id = ?1")
    fun userById(userId: Int): Optional<UserListDTO>

    /**
     * Find User By Name
     *
     * @param name String
     * @return Users
     */
    @Query("SELECT u, u.authorities FROM User u WHERE u.deletedAt is null AND u.name = ?1")
    fun findByName(name: String): Optional<User>

    /**
     * Find User By Email
     *
     * @param email String
     * @return Users
     */
    fun findByEmail(email:String): User?

    /**
     * Get User By Name Id
     *
     * @param userId Integer
     * @param name String
     * @return UserListDTO
     */
    @Query("SELECT u.name as name, u.email as email,u.profile as profile, u.type as type, u.phone as phone, u.dob as dob, u.address as address FROM User u WHERE u.name = ?2 AND u.id != ?1")
    fun userByNameAndId(userId: Int, name: String): Optional<UserListDTO>

    /**
     * Get User By Email Id
     *
     * @param userId Integer
     * @param email String
     * @return UserListDTO
     */
    @Query("SELECT u.name as name, u.email as email,u.profile as profile, u.type as type, u.phone as phone, u.dob as dob, u.address as address FROM User u WHERE u.email = ?2 AND u.id != ?1")
    fun userByEmailAndId(userId: Int, email: String): Optional<UserListDTO>
}