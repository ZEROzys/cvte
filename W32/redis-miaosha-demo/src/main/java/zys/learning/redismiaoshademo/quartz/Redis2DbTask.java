package zys.learning.redismiaoshademo.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import zys.learning.redismiaoshademo.service.RedisService;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class Redis2DbTask extends QuartzJobBean{

    private static final Logger LOGGER = LoggerFactory.getLogger(Redis2DbTask.class);
    private RedisService redisService;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public Redis2DbTask(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOGGER.info("定时任务开始------{}", sdf.format(new Date()));
        try {
//            redisService.transData2Db();
        } catch (Exception e) {
            LOGGER.error("redis同步数据库出错", e);
        }
    }
}
