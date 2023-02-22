package net.gaox.job.config;


import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * <p> 注册 XxlJob 执行器 </p>
 *
 * @author gaox·Eric
 * @date 2023-02-22 01:16
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "xxl.job.admin", name = "addresses")
@EnableConfigurationProperties(XxlJobConfig.class)
public class XxlJobAutoConfiguration {

    @Bean
    public XxlJobSpringExecutor xxlJobExecutor(XxlJobConfig xxlJobConfig, Environment environment) {
        log.info(">>>>>>>>>>> xxl-job config init.");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        XxlJobConfig.JobExecutorProperties executor = xxlJobConfig.getExecutor();
        String adminAddresses = xxlJobConfig.getAdmin().getAddresses();
        if (StringUtils.isBlank(adminAddresses)) {
            return null;
        }
        String appName = executor.getAppName();
        if (StringUtils.isBlank(appName)) {
            appName = environment.getProperty("spring.application.name");
        }

        xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
        xxlJobSpringExecutor.setAppname(appName);
        xxlJobSpringExecutor.setAddress(executor.getAddress());
        xxlJobSpringExecutor.setIp(executor.getIp());
        xxlJobSpringExecutor.setPort(executor.getPort());
        xxlJobSpringExecutor.setAccessToken(executor.getAccessToken());
        xxlJobSpringExecutor.setLogPath(executor.getLogPath());
        xxlJobSpringExecutor.setLogRetentionDays(executor.getLogRetentionDays());

        return xxlJobSpringExecutor;
    }

}
