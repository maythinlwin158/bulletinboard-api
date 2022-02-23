package Bulletinboard.service


import Bulletinboard.logging.Logging
import Bulletinboard.model.User
import Bulletinboard.repository.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
class DomainUserDetailsService(private val userRepository: UserRepository) : UserDetailsService, Logging {

    @Transactional
    override fun loadUserByUsername(username: String): UserDetails {
        log().debug("Authenticating $username")

        return userRepository.findByName(username)
                .map { createSpringSecurityUser(it) }
                .orElseThrow { UsernameNotFoundException("User $username was not found in the database") }
    }

    private fun createSpringSecurityUser(user: User):
            org.springframework.security.core.userdetails.User {

        val grantedAuthorities = user.authorities.map { SimpleGrantedAuthority(it.name) }
        return org.springframework.security.core.userdetails.User(
                user.name,
                user.password,
                grantedAuthorities
        )
    }
}
