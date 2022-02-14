package Bulletinboard.Controller

import Bulletinboard.DTO.PostEditDTO
import Bulletinboard.DTO.UserListDTO
import Bulletinboard.UserListDao
import Bulletinboard.form.PasswordForm
import Bulletinboard.form.PostForm
import Bulletinboard.form.UserEditForm
import Bulletinboard.form.UserForm
import Bulletinboard.model.Posts
import Bulletinboard.model.Users
import Bulletinboard.service.UserService
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.validation.Valid

@RestController
class UserController(private val userService: UserService) {

    @GetMapping("/users")
    fun getAllUsers(@RequestParam("name", defaultValue = "")name: String
                    , @RequestParam("email", defaultValue = "")email: String
                    , @RequestParam("created_from", defaultValue = "")created_from: String
                    , @RequestParam("created_to", defaultValue = "")created_to: String
                    , @RequestParam("page", defaultValue = "1")page: Int): List<UserListDTO> =
        userService.getAllUsers(name, email, created_from, created_to, PageRequest.of(page - 1, 3))

    @GetMapping("/user")
    fun getUser(): ResponseEntity<UserListDTO> {
        return userService.getUserById(1).map { user ->
            ResponseEntity.ok(user)
        }.orElse(ResponseEntity.notFound().build())
    }
    @PostMapping("/user")
    fun addUser(@Valid @RequestPart("user") userForm: UserForm, @RequestParam("image") multipartFile: MultipartFile): ResponseEntity<String> {
        return if (userService.createPost(userForm, multipartFile))
            ResponseEntity.ok("success")
        else ResponseEntity.badRequest().body("insert failed")
    }

    @GetMapping("/user/{userId}")
    fun editUser(@PathVariable(value = "userId")userId: Int): ResponseEntity<UserListDTO> {
        return userService.getUserById(userId).map { user ->
            ResponseEntity.ok(user)
        }.orElse(ResponseEntity.notFound().build())
    }

    @PutMapping("/user/{userId}")
    fun updateUser(@PathVariable(value = "userId")userId: Int, @Valid @RequestPart("user")userForm: UserEditForm, @RequestParam("image") multipartFile: MultipartFile): ResponseEntity<String> {
        return if (userService.updateUserById(userId, userForm, multipartFile)) ResponseEntity.ok("successfully update") else ResponseEntity.notFound().build()
    }

    @DeleteMapping("/user/{userId}")
    fun deleteUser(@PathVariable(value = "userId")userId: Int): ResponseEntity<Void> {
        return if (userService.userDeleteById(userId)) ResponseEntity<Void>(HttpStatus.OK) else ResponseEntity.notFound().build()
    }

    @PutMapping("/user/password")
    fun changePwd(@Valid @RequestBody passwordForm: PasswordForm): ResponseEntity<String> {
        return userService.changePwd(passwordForm)
    }
}