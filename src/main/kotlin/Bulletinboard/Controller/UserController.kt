package Bulletinboard.Controller

import Bulletinboard.DTO.UserListDTO
import Bulletinboard.form.PasswordForm
import Bulletinboard.form.UserEditForm
import Bulletinboard.form.UserForm
import Bulletinboard.service.UserService
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.validation.Valid

@RestController
class UserController(private val userService: UserService) {

    /**
     * Get ALL Users
     *
     * @param name String
     * @param email String
     * @param page Integer
     * @return UserListDTO
     */
    @GetMapping("/users")
    fun getAllUsers(@RequestParam("name", defaultValue = "")name: String
                    , @RequestParam("email", defaultValue = "")email: String
                    , @RequestParam("page", defaultValue = "1")page: Int): List<UserListDTO> =
        userService.getAllUsers(name, email, PageRequest.of(page - 1, 5))

    /**
     * Get User By Login User Id
     *
     * @return UserListDTO
     */
    @GetMapping("/user")
    fun getUser(): ResponseEntity<UserListDTO> {
        return userService.getUserById(1).map { user ->
            ResponseEntity.ok(user)
        }.orElse(ResponseEntity.notFound().build())
    }

    /**
     * Create User
     *
     * @param userForm UserForm
     * @param multipartFile MultipartFile
     * @return String
     */
    @PostMapping("/user")
    fun addUser(@Valid @RequestPart("user") userForm: UserForm, @RequestParam("image") multipartFile: MultipartFile): ResponseEntity<String> {
        if (multipartFile.isEmpty) {
            return ResponseEntity.badRequest().body("profile can't be null")
        }
        if (userForm.name?.let { userService.isUserExists(it) } == true) {
            return ResponseEntity.badRequest().body("name already exists")
        }
        if (userForm.email?.let { userService.findByEmail(it) } == false) {
            return ResponseEntity.badRequest().body("email already exists")
        }
        return if (userService.createUser(userForm, multipartFile))
            ResponseEntity.ok("success")
        else ResponseEntity.badRequest().body("insert failed")
    }

    /**
     * Get User By Id
     *
     * @param userId Integer
     * @return UserListDTO
     */
    @GetMapping("/user/{userId}")
    fun editUser(@PathVariable(value = "userId")userId: Int): ResponseEntity<UserListDTO> {
        return userService.getUserById(userId).map { user ->
            ResponseEntity.ok(user)
        }.orElse(ResponseEntity.notFound().build())
    }

    /**
     * Update User By user Id
     *
     * @param userId Integer
     * @param userForm UserEditForm
     * @param multipartFile MultipartFile
     * @return String
     */
    @PutMapping("/user/{userId}")
    fun updateUser(@PathVariable(value = "userId")userId: Int, @Valid @RequestPart("user")userForm: UserEditForm, @RequestParam("image") multipartFile: MultipartFile): ResponseEntity<String> {
        if(multipartFile.isEmpty) {
            return ResponseEntity.badRequest().body("profile can't be null")
        }
        if (userForm.name?.let { userService.findByNameAndId(userId, it) } == true) {
            return ResponseEntity.badRequest().body("name already exists")
        }
        if (userForm.email?.let { userService.findByEmailAndId(userId, it) } == true) {
            return ResponseEntity.badRequest().body("email already exists")
        }
        return if (userService.updateUserById(userId, userForm, multipartFile)) ResponseEntity.ok("successfully update") else ResponseEntity.notFound().build()
    }

    /**
     * Delete User By Id
     *
     * @param userId Integer
     */
    @DeleteMapping("/user/{userId}")
    fun deleteUser(@PathVariable(value = "userId")userId: Int): ResponseEntity<Void> {
        return if (userService.userDeleteById(userId)) ResponseEntity<Void>(HttpStatus.OK) else ResponseEntity.notFound().build()
    }

    /**
     * Change Password
     *
     * @param passwordForm passwordForm
     * @return String
     */
    @PutMapping("/user/password")
    fun changePwd(@Valid @RequestBody passwordForm: PasswordForm): ResponseEntity<String> {
        return userService.changePwd(passwordForm)
    }
}