package com.tdtech.docking.common.util.dynamicScheduledAnnotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 定时任务动态配置注解
 *
 *
 * @author fwx1093096
 * @since 2023-06-05
 *
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DynamicScheduled {

    String scheduledKey() default "";

    String cronValue() default "";

    long fixedRate() default -1;

    long fixedDelay() default -1;
}
