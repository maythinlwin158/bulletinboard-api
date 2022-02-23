package Bulletinboard.service

import Bulletinboard.DTO.UserDto
import Bulletinboard.DTO.UserListDTO
import Bulletinboard.form.PasswordForm
import Bulletinboard.form.UserEditForm
import Bulletinboard.form.UserForm
import Bulletinboard.model.Authority
import Bulletinboard.repository.AuthorityRepository
import Bulletinboard.security.USER
import Bulletinboard.logging.Logging
import Bulletinboard.model.User
import Bulletinboard.repository.UserRepository
import Bulletinboard.util.FileUploadUtil
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import java.math.BigInteger
import java.security.MessageDigest
import java.time.LocalDateTime
import java.util.*


@Service
class UserService(
    private  val userRepository: UserRepository,
    private val authorityRepository: AuthorityRepository,
    private val passwordEncoder: PasswordEncoder
) : Logging {

//    val md = MessageDigest.getInstance("MD5")
//
    /**
     * Get ALl Users
     *
     * @param name String
     * @param email String
     * @param pageable Pageable
     * @return List<UserListDTO> UserListDTO
     */
    fun getAllUsers(name: String, email: String, pageable: Pageable): List<UserListDTO> = userRepository.findAllUsers(name, email, pageable)

    /**
     * Find User By Name
     *
     * @param name String
     * @return Users
     */
    fun findByName(name: String): Boolean { return userRepository.findByName(name) == null }

    /**
     * Find User By email
     *
     * @param email String
     * @return Users
     */
    fun findByEmail(email: String): Boolean { return userRepository.findByEmail(email) == null }

    /**
     * Add User Into User Table
     *
     * @param userForm UserForm
     * @param multipartFile MultipartFile
     */
    fun createUser(userForm: UserForm, multipartFile: MultipartFile): Boolean {

        //Get Original Image File Name
        val fileName: String? = multipartFile.originalFilename?.let { StringUtils.cleanPath(it) }
        if(fileName.isNullOrEmpty()) {
            return false
        }

        val auth = SecurityContextHolder.getContext().authentication
        val authUser = userRepository.findByName(auth.name)
        val authId = authUser.get().id

        val encryptPassword = passwordEncoder.encode(userForm.password)
        val authorities = mutableSetOf<Authority>()
        authorityRepository.findById(USER).ifPresent { authorities.add(it) }

        var user: User = User()
        user.name = userForm.name
        user.email = userForm.email
        user.password = encryptPassword
        user.phone = userForm.phone
        user.profile = fileName
        user.createdUserId = authId
        user.updatedUserId = authId
        user.authorities = userForm.authorities?.let { authorities ->
            authorities.map { authorityRepository.findById(it) }
                .filter { it.isPresent }
                .mapTo(mutableSetOf()) { it.get() }
        } ?: mutableSetOf()

        return try {
            val savedUser = userRepository.save(user)
            val uploadDir = "user-photos/" + savedUser.id + "/image"
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile)
            userRepository.save(savedUser.copy(profile = "$uploadDir/$fileName"))
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Get User By Name And Id
     *
     * @param userId Integer
     * @param name String
     * @return Boolean
     */
    fun findByNameAndId(userId: Int, name: String): Boolean { return userRepository.userByNameAndId(userId, name).isPresent }

    /**
     * Get Post By Email And Id
     *
     * @param userId Integer
     * @param email String
     * @return Boolean
     */
    fun findByEmailAndId(userId: Int, email: String): Boolean { return userRepository.userByEmailAndId(userId , email).isPresent }

    /**
     * Get Login User By ID
     *
     * @param userId Integer
     * @return UserListDTO
     */
    fun getUserById(userId: Int): Optional<UserListDTO> = userRepository.userById(userId)

    /**
     * Update User Into User Table
     *
     * @param userId Integer
     * @param multipartFile MultipartFile
     * @return true/false Boolean
     */
    fun updateUserById(userId: Int, user: UserEditForm, multipartFile: MultipartFile): Boolean = userRepository.findById(userId).map { oldUser ->

        val fileName: String? = multipartFile.originalFilename?.let { StringUtils.cleanPath(it) }

        val auth = SecurityContextHolder.getContext().authentication
        val authUser = userRepository.findByName(auth.name)
        val authId = authUser.get().id

        val newUser: User = oldUser.copy(name = user.name, email = user.email, type = user.type, phone = user.phone
            , dob = user.dob, address = user.address, profile = fileName, updatedUserId = authId)
        try {
            userRepository.save(newUser)
            val uploadDir = "user-photos/" + oldUser.id + "/image"
            if (fileName != null) {
                FileUploadUtil.saveFile(uploadDir, fileName, multipartFile)
            }
            userRepository.save(newUser.copy(profile = "$uploadDir/$fileName"))
            true
        } catch (e: Exception) {
            false
        }
    }.orElse(false)

    /**
     * User Delete By User Id
     *
     * @param userId Integer
     * @return true/false Boolean
     */
    fun userDeleteById(userId: Int): Boolean {
        return userRepository.findById(userId).map { oldUser ->
            val newUser = oldUser.copy(deletedAt = LocalDateTime.now(), deletedUserId = 1)
            userRepository.save(newUser)
            true
        }.orElse(false)
    }

    /**
     * Change Password By Login User Id
     *
     * @param passwordForm PasswordForm
     * @return ResponseEntity
     */
    fun changePwd(passwordForm: PasswordForm): ResponseEntity<String> {
        val auth = SecurityContextHolder.getContext().authentication
        val authUser = userRepository.findByName(auth.name)
        return userRepository.findById(authUser.get().id).map { user ->
            if (passwordEncoder.matches(passwordForm.password, user.password)) {
                userRepository.save(
                    user.copy(
                        password = passwordEncoder.encode(passwordForm.newPassword)
                    )
                )
                ResponseEntity.ok("successfully update")
            }
            else ResponseEntity.badRequest().body("old password doesn't match")
        }.orElse(ResponseEntity.notFound().build()
        )
    }

    fun isUserExists(name: String): Boolean = userRepository.findByName(name).isPresent
}