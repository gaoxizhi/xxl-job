package net.gaox.job.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p> Xxl 增强注解 </p>
 *
 * @author gaox·Eric
 * @date 2023-02-21 21:22
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface XxlRegister {

    String cron();

    String jobDesc() default "default jobDesc";

    String author() default "gaox";

    /**
     * 默认为 ROUND 轮询方式
     * 可选： FIRST 第一个
     */
    String executorRouteStrategy() default "ROUND";

    /**
     * 状态：0=未生效，1=运行
     */
    int triggerStatus() default 0;
}
