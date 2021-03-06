package Bulletinboard

import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@SpringBootApplication
class BulletinboardApplication

fun main(args: Array<String>) {
//	runApplication<BulletinboardApplication>(*args)
	runApplication<BulletinboardApplication>(*args) {
		setBannerMode(Banner.Mode.OFF)
	}
}