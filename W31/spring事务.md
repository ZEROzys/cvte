## spring事务

### spring事务管理的两种方式

spring支持编程式事务和声明式事务

+ 编程式事务使用 TransactionTemplate

  ```kotlin
  @Autowired
      private val transactionTemplate: TransactionTemplate? = null
  
  fun transferAccount(account: Long) {
          transactionTemplate!!.execute { transactionStatus ->
              try {
                  val user1 = userRepository!!.findUserByUsername("jack")
                  user1.balance = user1.balance + account
                  userRepository.save(user1)
                  // 转账出现异常，回滚事务
  //                val i = 10 / 0
                  val user2 = userRepository.findUserByUsername("tom")
                  if (user2.balance >= account) {
                      user2.balance = user2.balance - account
                      userRepository.save(user2)
                  } else
                      transactionStatus.setRollbackOnly()
              } catch (e: Exception) {
                  transactionStatus.setRollbackOnly()
                  e.printStackTrace()
              }
          }
      }
  ```

+ 声明式事务建立在AOP之上，本质是对方法前后进行拦截，然后在目标方法开始之前创建或者加入一个事务，在执行完目标方法之后根据执行情况提交或者回滚事务，可以基于配置文件或者基于@Transactional注解

### 常用配置

|        参数功能        | 功能描述                                                     |
| :--------------------: | ------------------------------------------------------------ |
|        readOnly        | 该属性用于设置当前事务是否为只读事务，设置为true表示只读，false则表示可读写，默认值为false。例如：@Transactional(readOnly=true) |
|      rollbackFor       | 该属性用于设置需要进行回滚的异常类数组，当方法中抛出指定异常数组中的异常时，则进行事务回滚。例如：指定单一异常类：@Transactional(rollbackFor=RuntimeException.class)<br />指定多个异常类：@Transactional(rollbackFor={RuntimeException.class, Exception.class}) |
|  rollbackForClassName  | 该属性用于设置需要进行回滚的异常类名称数组，当方法中抛出指定异常名称数组中的异常时，则进行事务回滚。例如：指定单一异常类名称@Transactional(rollbackForClassName=”RuntimeException”)<br />指定多个异常类名称：@Transactional(rollbackForClassName={“RuntimeException”,”Exception”}) |
|     noRollbackFor      | 该属性用于设置不需要进行回滚的异常类数组，当方法中抛出指定异常数组中的异常时，不进行事务回滚。例如：指定单一异常类：@Transactional(noRollbackFor=RuntimeException.class)<br />指定多个异常类：@Transactional(noRollbackFor={RuntimeException.class, Exception.class}) |
| noRollbackForClassName | 该属性用于设置不需要进行回滚的异常类名称数组，当方法中抛出指定异常名称数组中的异常时，不进行事务回滚。例如：指定单一异常类名称：@Transactional(noRollbackForClassName=”RuntimeException”)<br />指定多个异常类名称：@Transactional(noRollbackForClassName={“RuntimeException”,”Exception”}) |
|      propagation       | 该属性用于设置事务的传播行为。例如：@Transactional(propagation=Propagation.NOT_SUPPORTED,<br />readOnly=true) |
|       isolation        | 该属性用于设置底层数据库的事务隔离级别，事务隔离级别用于处理多事务并发的情况，通常使用数据库的默认隔离级别即可，基本不需要进行设置 |
|        timeout         | 该属性用于设置事务的超时秒数，默认值为-1表示永不超时         |

### 事务特性

#### 隔离级别

+ Isolation.DEFAULT：默认值，表示底层数据库的默认隔离级别
+ Isolation.READ_UNCOMMITTED：表示一个事务可以读取另一个事务未提交的数据，该级别不能防止脏读、不可重复读和幻读
+ Isolation.COMMITTED：表示一个事务只能读取另一个事务已经提交的数据，可以防止脏读
+ Isolation.REPEATABLE_READ：可以防止脏读和不可重复读
+ Isolation.SERIALIZABLE：依次逐个执行，可以防止脏读、不可重复读、幻读

#### 事务传播行为

事务的传播行为是指，如果在开始当前事务之前，一个事务上下文已经存在，此时有若干选项可以指定一个事务性方法的执行行为。

+ Propagation.REQUIRED：默认值，如果当前存在事务，则加入该事务；如果当前没有事务，则创建一个新的事务

+ ```java
  public enum Propagation {
      REQUIRED(0),
      SUPPORTS(1),
      MANDATORY(2),
      REQUIRES_NEW(3),
      NOT_SUPPORTED(4),
      NEVER(5),
      NESTED(6);
  
      private final int value;
  
      private Propagation(int value) {
          this.value = value;
      }
  
      public int value() {
          return this.value;
      }
  }
  ```

#### 事务超时行为

指一个事务所允许执行的最长时间，如果超过该时间限制但事务还没有完成，则自动回滚事务。

#### 事务回滚规则

默认配置下，spring只有在抛出的异常为运行时unchecked异常时才回滚该事务，也就是抛出的异常为RuntimeException的子类(Errors也会导致事务回滚)，而抛出checked异常则不会导致事务回滚。可以明确的配置在抛出哪些异常时回滚事务，包括checked异常。也可以明确定义那些异常抛出时不回滚事务。还可以编程性的通过setRollbackOnly()方法来指示一个事务必须回滚，在调用完setRollbackOnly()后你所能执行的唯一操作就是回滚。

#### 事务只读属性
只读事务用于客户代码只读但不修改数据的情形，只读事务用于特定情景下的优化，比如使用Hibernate的时候。 
默认为读写事务。





#### 参考

https://blog.csdn.net/wkl305268748/article/details/77619367

https://www.cnblogs.com/nnngu/p/8627662.html