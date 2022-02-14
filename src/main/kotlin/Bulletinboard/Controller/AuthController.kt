package Bulletinboard.Controller

import Bulletinboard.DTO.LoginDTO
import Bulletinboard.DTO.Message
import Bulletinboard.form.UserForm
import Bulletinboard.model.Users
import Bulletinboard.repository.UserRepository
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("api")
class AuthController(private val userRepository: UserRepository) {

//    @PostMapping("register")
//    fun register(@RequestBody body: UserForm): ResponseEntity<Users> {
//print(body);
//        val passwordEncoder = BCryptPasswordEncoder()
//
//        val user = Users()
//        user.name = body.name
//        user.email = body.email
//        user.password = passwordEncoder.encode(body.password)
//
//        return ResponseEntity.ok(this.userRepository.save(user))
//    }

    @PostMapping("login")
    fun login(@RequestBody body: LoginDTO, response: HttpServletResponse): ResponseEntity<Any> {
        val user = this.userRepository.findByEmail(body.email)
            ?: return ResponseEntity.badRequest().body(Message("user not found!"))

        val md = MessageDigest.getInstance("MD5")
        if (user.password != BigInteger(1, md.digest(body.password?.toByteArray())).toString(16).padStart(32, '0')) {
            return ResponseEntity.badRequest().body(Message("invalid password!"))
        }

        val issuer = user.id.toString()

        val jwt = Jwts.builder()
            .setIssuer(issuer)
            .setExpiration(Date(System.currentTimeMillis() + 60 * 24 * 1000)) // 1 day
            .signWith(SignatureAlgorithm.HS512, "secret").compact()

        val cookie = Cookie("jwt", jwt)
        cookie.isHttpOnly = true

        response.addCookie(cookie)

        return ResponseEntity.ok(Message("success"))
    }

    @GetMapping("user")
    fun user(@CookieValue("jwt") jwt: String?): ResponseEntity<Any> {
        try {
            if (jwt == null) {
                return ResponseEntity.status(401).body(Message("unauthenticated"))
            }

            val body = Jwts.parser().setSigningKey("secret").parseClaimsJws(jwt).body

            return ResponseEntity.ok(this.userRepository.userById(body.issuer.toInt()))
        } catch (e: Exception) {
            return ResponseEntity.status(401).body(Message("unauthenticated"))
        }
    }

    @PostMapping("logout")
    fun logout(response: HttpServletResponse): ResponseEntity<Any> {
        val cookie = Cookie("jwt", "")
        cookie.maxAge = 0

        response.addCookie(cookie)

        return ResponseEntity.ok(Message("success"))
    }
}