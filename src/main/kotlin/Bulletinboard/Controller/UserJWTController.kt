package Bulletinboard.Controller

import Bulletinboard.logging.Logging
import Bulletinboard.security.jwt.JWTFilter
import Bulletinboard.security.jwt.TokenProvider
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
class UserJWTController(
    private val tokenProvider: TokenProvider,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder
): Logging {
    @PostMapping("/authenticate")
    fun authorize( @RequestBody loginDto: LoginDto): ResponseEntity<JWTToken> {
        log().info("enter ${loginDto.username} and pwd ${loginDto.password}")
        val authenticationToken = UsernamePasswordAuthenticationToken(loginDto.username, loginDto.password)
        log().info("auth $authenticationToken")
        val authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken)
        SecurityContextHolder.getContext().authentication = authentication
        val jwt = tokenProvider.createToken(authentication)
        log().info("jwt is:$jwt")

        val httpHeaders = HttpHeaders()
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer $jwt")
        return ResponseEntity(JWTToken(jwt), httpHeaders, HttpStatus.OK)
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    class JWTToken(@get:JsonProperty("id_token") var idToken: String?)
}

data class LoginDto(
    var username: String? = null,
    var password: String? = null
)