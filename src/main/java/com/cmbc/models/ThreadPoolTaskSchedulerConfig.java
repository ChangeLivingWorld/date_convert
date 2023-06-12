package com.tdtech.docking.common.util.dynamicScheduledAnnotation;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

/**
 * @author fwx1093096
 * @since 2023/06/12/15:39
 */
@Component
public class ThreadPoolTaskSchedulerConfig {

    @Bean(value = "myThreadPoolTaskScheduler")
    public ThreadPoolTaskScheduler scheduler() {
        ThreadPoolTaskScheduler executor = new ThreadPoolTaskScheduler();
        executor.setPoolSize(Runtime.getRuntime().availableProcessors() * 2);
        executor.setThreadNamePrefix("task-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        return executor;
    }
}
