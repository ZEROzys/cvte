package zys.learning.kotlintransactiondemo

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

import zys.learning.kotlintransactiondemo.service.UserService

@RunWith(SpringRunner::class)
@SpringBootTest
class KotlinTransactionDemoApplicationTests (
) {

	@Autowired
	lateinit var userService: UserService

	@Test
	fun contextLoads() {
	}


	@Test
	fun testTransferAccount() {
//		userService.transferAccountWithAnnotation(30)
		userService.transferAccount(30)
	}
}
