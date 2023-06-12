package com.tdtech.docking.common.util.dynamicScheduledAnnotation;

import com.tdtech.dems.common.util.StringUtils;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

/**
 * 定时任务动态注册类
 * Enables Spring's dynamic scheduled task execution capability,
 * annotation {@link DynamicScheduled @DynamicScheduled} is requested.
 *
 *
 *
 * @author wWX536858
 * @since 2022/02/15 16:31
 */
@Component
@Slf4j
public class DynamicScheduledTaskRegistrar {

    /**
     * 管理定时任务对应的Future，key为任务名称
     */
    private final Map<String, Future<?>> taskFutures = new ConcurrentHashMap<>(8);

    @Resource(name = "myThreadPoolTaskScheduler")
    private ThreadPoolTaskScheduler scheduler;

    /**
     * 添加定时任务
     * @param task       任务执行体
     * @return true 添加成功 false 添加失败
     */
    public boolean addCronTask(ScheduledTaskInfo scheduledTaskInfo, Runnable task) {
        String scheduledKey = scheduledTaskInfo.getScheduledKey();
        String cronValue = scheduledTaskInfo.getCronValue();
        Long fixedDelay = scheduledTaskInfo.getFixedDelay();
        Long fixedRate = scheduledTaskInfo.getFixedRate();
        Future<?> future = taskFutures.get(scheduledKey);
        if (future != null) {
            future.cancel(false);
        }
        ScheduledFuture<?> schedule = null;
        if (!StringUtils.isNullBlank(cronValue)) {
            schedule = scheduler.schedule(task, new CronTrigger(cronValue));
        } else if (fixedRate != null) {
            schedule = scheduler.scheduleAtFixedRate(task, fixedRate);
        } else if (fixedDelay != null) {
            schedule = scheduler.scheduleWithFixedDelay(task, fixedDelay);
        }
        if (schedule == null) {
            log.error("add fails. duplicate task {}", scheduledTaskInfo);
            return false;
        } else {
            log.info("create task success {}", scheduledTaskInfo);
            taskFutures.put(scheduledKey, schedule);
        }

        return true;
    }

    /**
     * 根据任务名删除任务
     *
     * @param taskName   任务名
     */
    public synchronized void deleteCronTaskByName(String taskName) {
        Future<?> future = taskFutures.get(taskName);
        if (future == null) {
            log.info("no such task {},stop deleting process.", taskName);
            return;
        }
        boolean cancel = future.cancel(true);
        if (!cancel) {
            future.cancel(true);
        }
        taskFutures.remove(taskName);
    }
}
