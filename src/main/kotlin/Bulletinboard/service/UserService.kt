package Bulletinboard.service

import Bulletinboard.DTO.UserListDTO
import Bulletinboard.form.PasswordForm
import Bulletinboard.form.UserEditForm
import Bulletinboard.form.UserForm
import Bulletinboard.model.Users
import Bulletinboard.repository.UserRepository
import Bulletinboard.util.FileUploadUtil
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.math.BigInteger
import java.security.MessageDigest
import java.time.LocalDateTime
import java.util.*


@Service
class UserService(private val userRepository: UserRepository) {

    val md = MessageDigest.getInstance("MD5")
    fun getAllUsers(name: String, email: String, created_from: String, created_to: String, pageable: Pageable): List<UserListDTO> = userRepository.findAllUsers(name, email, created_from, created_to, pageable)

    fun createPost(userForm: UserForm, multipartFile: MultipartFile): Boolean {

        //Get Original Image File Name
        val fileName: String? = multipartFile.originalFilename?.let { StringUtils.cleanPath(it) }
        if(fileName.isNullOrEmpty()) {
            return false
        }

        //Encrypt Password
        userForm.password = BigInteger(1, md.digest(userForm.password?.toByteArray())).toString(16).padStart(32, '0')

        var user: Users = Users()
        user.name = userForm.name
        user.email = userForm.email
        user.password = userForm.password
        user.phone = userForm.phone
        user.profile = fileName
        user.createdUserId = 1
        user.updatedUserId = 1

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

    fun getUserById(userId: Int): Optional<UserListDTO> = userRepository.userById(userId)

    fun updateUserById(userId: Int, user: UserEditForm, multipartFile: MultipartFile): Boolean = userRepository.findById(userId).map { oldUser ->

        val fileName: String? = multipartFile.originalFilename?.let { StringUtils.cleanPath(it) }

        val newUser: Users = oldUser.copy(name = user.name, email = user.email, type = user.type, phone = user.phone
            , dob = user.dob, address = user.address, profile = fileName)
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

    fun userDeleteById(userId: Int): Boolean {
        return userRepository.findById(userId).map { oldUser ->
            val newUser = oldUser.copy(deletedAt = LocalDateTime.now(), deletedUserId = 1)
            userRepository.save(newUser)
            true
        }.orElse(false)
    }

    fun changePwd(passwordForm: PasswordForm): ResponseEntity<String> {
        return userRepository.findById(17).map { user ->
            if (user.password == BigInteger(1, md.digest(passwordForm.password?.toByteArray())).toString(16).padStart(32, '0')) {
                userRepository.save(
                    user.copy(
                        password = BigInteger(1, md.digest(passwordForm.newPassword?.toByteArray())).toString(16)
                            .padStart(32, '0')
                    )
                )
                ResponseEntity.ok("successfully update")
            }
            else ResponseEntity.badRequest().build()
        }.orElse(ResponseEntity.notFound().build()
        )
    }
}