package com.tdtech.docking.common.util.dynamicScheduledAnnotation;

import java.lang.reflect.Method;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ScheduledTaskInfo {

    /**
     * cron表达式  在配置文件中的名称
     */
    private String scheduledKey;

    /**
     * cron表达式
     */
    private String cronValue;

    private Long fixedRate;

    private Long fixedDelay;

    private Object bean;

    private Method method;
}