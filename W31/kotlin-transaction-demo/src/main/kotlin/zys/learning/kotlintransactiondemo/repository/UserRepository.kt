package zys.learning.kotlintransactiondemo.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import zys.learning.kotlintransactiondemo.entity.User

@Repository
interface UserRepository: CrudRepository<User, Long> {
    fun findUserByUsername(username: String): User
}