package zys.learning.kotlintransactiondemo.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.support.TransactionCallback
import org.springframework.transaction.support.TransactionTemplate
import zys.learning.kotlintransactiondemo.entity.User
import zys.learning.kotlintransactiondemo.repository.UserRepository

@Service
class UserService1 {

    @Autowired
    private val transactionTemplate: TransactionTemplate? = null

    @Autowired
    private val userRepository: UserRepository? = null

    @Throws(Exception::class)
    fun transfer(account: Long) {
        transactionTemplate!!.execute { transactionStatus ->
            var res: Any = true
            try {
                val user1 = userRepository!!.findUserByUsername("jack")
                user1.balance = user1.balance + account
                userRepository.save(user1)
                // 转账出现异常，回滚事务
                val i = 10 / 0
                val user2 = userRepository.findUserByUsername("tom")
                if (user2.balance >= account) {
                    user2.balance = user2.balance - account
                    userRepository.save(user2)
                } else
                    transactionStatus.setRollbackOnly()
            } catch (e: Exception) {
                res = false
                transactionStatus.setRollbackOnly()
                e.printStackTrace()

            }

            res
        }
    }
}
