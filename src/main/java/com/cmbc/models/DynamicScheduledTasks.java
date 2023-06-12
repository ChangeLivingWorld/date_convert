package com.tdtech.docking.common.util.dynamicScheduledAnnotation;

import com.tdtech.dems.common.util.StringUtils;
import com.tdtech.dems.common.util.Strings;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author fwx1093096
 * @since 2023/05/29/10:12
 */
@Slf4j
@Component
public class DynamicScheduledTasks implements CommandLineRunner {

    @Autowired
    private DynamicScheduledTaskRegistrar taskRegistrar;

    @Autowired
    private Environment environment;

    @Override
    public void run(String... args) {
        configureTasks();
    }

    @EventListener(EnvironmentChangeEvent.class)
    public void listener(EnvironmentChangeEvent event) {
        for (String refreshedKey : event.getKeys()) {
            try {
                String expression = environment.getProperty(refreshedKey);
                log.info("task configuration changed {} {}", refreshedKey, expression);
                ScheduledTaskInfo methodInScheduledTask = getMethodInScheduledTask(refreshedKey, expression);
                if(methodInScheduledTask == null || methodInScheduledTask.getMethod() == null) {
                    return;
                }
                String cronValue = methodInScheduledTask.getCronValue();
                Long fixedDelay = methodInScheduledTask.getFixedDelay();
                Long fixedRate = methodInScheduledTask.getFixedRate();
                Method method = methodInScheduledTask.getMethod();
                Object bean = methodInScheduledTask.getBean();
                if ((!StringUtils.isNullBlank(cronValue) && cronValue.equals(Strings.DASH))
                        || (fixedDelay != null && fixedDelay == -1) || (fixedRate != null && fixedRate == -1)) {
                    taskRegistrar.deleteCronTaskByName(methodInScheduledTask.getScheduledKey());
                    return;
                }
                if (method == null) {
                    return;
                }
                Runnable runnable = () -> {
                    try {
                        method.invoke(bean);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        log.error("execute method error:" + e);
                    }
                };
                this.taskRegistrar.addCronTask(methodInScheduledTask, runnable);
            } catch (Exception e) {
                log.error("exception occurs:" + e);
                e.printStackTrace();
            }
        }
    }

    public ScheduledTaskInfo getMethodInScheduledTask(String refreshedKey, String expression) {
        ScheduledTaskInfo scheduledTaskInfo = new ScheduledTaskInfo();
        Reflections reflections = new Reflections("com.tdtech.docking");//包名且不可忘记，不然扫描全部项目包，包括引用的jar
        //获取带Service注解的类
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Component.class);
        for (Class clazz : typesAnnotatedWith) {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                //判断带自定义注解MyAnnontation的method
                if (!method.isAnnotationPresent(DynamicScheduled.class)) {
                    return scheduledTaskInfo;
                }
                DynamicScheduled annotation = method.getAnnotation(DynamicScheduled.class);
                String scheduledKey = annotation.scheduledKey();
                String cronValue = annotation.cronValue();
                long fixedRate = annotation.fixedRate();
                long fixedDelay = annotation.fixedDelay();
                //根据入参WayCode比较method注解上的WayCode,两者值相同才执行该method
                if (null == scheduledKey || !scheduledKey.equals(refreshedKey)) {
                    return scheduledTaskInfo;
                }
                try {
                    scheduledTaskInfo.setScheduledKey(scheduledKey);
                    if (!StringUtils.isNullBlank(cronValue) && fixedRate == -1 && fixedDelay == -1) {
                        scheduledTaskInfo.setCronValue(
                                StringUtils.isNullBlank(expression) ? cronValue : expression);
                    } else if (fixedRate != -1 && fixedDelay == -1 && StringUtils.isNullBlank(cronValue)) {
                        scheduledTaskInfo.setFixedRate(
                                StringUtils.isNullBlank(expression) ? fixedRate : Long.parseLong(expression));
                    } else if (fixedDelay != -1 && fixedRate == -1 && StringUtils.isNullBlank(cronValue)) {
                        scheduledTaskInfo.setFixedDelay(
                                StringUtils.isNullBlank(expression) ? fixedDelay : Long.parseLong(expression));
                    } else {
                        log.error("input scheduled param error, only support cron or fixRate or initialDelay.");
                        return scheduledTaskInfo;
                    }
                    //执行method
                    Object bean = SpringContextUtil.getBean(clazz);
                    scheduledTaskInfo.setMethod(method);
                    scheduledTaskInfo.setBean(bean);
                    return scheduledTaskInfo;
                } catch (Exception e) {
                    log.error("execute method error:" + e);
                }
            }
        }
        return scheduledTaskInfo;
    }

    public void configureTasks() {
        ScheduledTaskInfo scheduledTaskInfo = new ScheduledTaskInfo();
        //包名且不可忘记，不然扫描全部项目包，包括引用的jar
        Reflections reflections = new Reflections("com.tdtech.docking");
        //获取带Component注解的类
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Component.class);
        for (Class clazz : typesAnnotatedWith) {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                //判断带自定义注解DynamicScheduled的method
                if (!method.isAnnotationPresent(DynamicScheduled.class)) {
                    continue;
                }
                DynamicScheduled annotation = method.getAnnotation(DynamicScheduled.class);
                String scheduledKey = annotation.scheduledKey();
                String cronValue = annotation.cronValue();
                long fixedRate = annotation.fixedRate();
                long fixedDelay = annotation.fixedDelay();
                //根据入参WayCode比较method注解上的WayCode,两者值相同才执行该method
                if (null == scheduledKey) {
                    continue;
                }
                try {
                    Object bean = SpringContextUtil.getBean(clazz);
                    String scheduleValueConfig = environment.getProperty(scheduledKey);
                    scheduledTaskInfo.setScheduledKey(scheduledKey);
                    if (!StringUtils.isNullBlank(cronValue) && fixedRate == -1 && fixedDelay == -1) {
                        cronValue = StringUtils.isNullBlank(scheduleValueConfig) ? cronValue : scheduleValueConfig;
                        if (cronValue.equals(Strings.DASH)) {
                            taskRegistrar.deleteCronTaskByName(scheduledKey);
                            continue;
                        }
                        scheduledTaskInfo.setCronValue(cronValue);
                    } else if (fixedRate != -1 && fixedDelay == -1 && StringUtils.isNullBlank(cronValue)) {
                        scheduledTaskInfo.setFixedRate(
                                StringUtils.isNullBlank(scheduleValueConfig) ? fixedRate : Long.parseLong(
                                        scheduleValueConfig));
                    } else if (fixedDelay != -1 && fixedRate == -1 && StringUtils.isNullBlank(cronValue)) {
                        scheduledTaskInfo.setFixedDelay(
                                StringUtils.isNullBlank(scheduleValueConfig) ? fixedDelay : Long.parseLong(
                                scheduleValueConfig));
                    } else {
                        log.error("input scheduled param error, only support cron or fixRate or initialDelay.");
                        continue;
                    }

                    Runnable r = () -> {
                        try {
                            method.invoke(bean);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            log.error("exception occurs:" + e);
                            e.printStackTrace();
                        }
                    };
                    taskRegistrar.addCronTask(scheduledTaskInfo, r);
                } catch (Exception e) {
                    log.error("execute method error:" + e);
                    e.printStackTrace();
                }
            }
        }
    }
}
