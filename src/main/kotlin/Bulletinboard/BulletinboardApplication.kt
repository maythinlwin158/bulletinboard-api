package Bulletinboard

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BulletinboardApplication

fun main(args: Array<String>) {
	runApplication<BulletinboardApplication>(*args)
}
