package zys.practice.seckillconsumer.config;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zys.practice.seckillconsumer.task.Redis2DbTask;

@Configuration
public class QuartzConfig {

    private static final String TASK_IDENTITY = "REDIS2DB";

    @Bean
    public JobDetail quartzDetail(){
        return JobBuilder.newJob(Redis2DbTask.class).
                withIdentity(TASK_IDENTITY).storeDurably().build();
    }

    @Bean
    public Trigger quartzTrigger(){
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
//                .withIntervalInHours(2)  //两个小时执行一次
                .withIntervalInSeconds(30)
                .withRepeatCount(5);
        return TriggerBuilder.newTrigger().forJob(quartzDetail())
                .withIdentity(TASK_IDENTITY)
                .withSchedule(scheduleBuilder)
                .build();
    }
}
