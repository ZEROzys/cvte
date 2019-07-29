package zys.learning.kotlintransactiondemo.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import zys.learning.kotlintransactiondemo.entity.User
import zys.learning.kotlintransactiondemo.repository.UserRepository

@RestController
@RequestMapping("/api")
class UserController (
        private val userRepository: UserRepository
) {

    @GetMapping("/hello")
    fun hello(): String {
        return "helloworld"
    }

//    @GetMapping("/user/{username}")
//    fun getUserByUsername(@PathVariable("username") username: String): User {
//        return userRepository.findUserByUsername(username)
//    }
}