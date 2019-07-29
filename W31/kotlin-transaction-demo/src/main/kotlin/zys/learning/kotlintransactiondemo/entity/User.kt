package zys.learning.kotlintransactiondemo.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.NotBlank

@Entity
data class User (
       @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
       var id: Long? = null,

       @NotBlank
       var username: String = "",

       @NotBlank
       var balance: Long = 0L
)