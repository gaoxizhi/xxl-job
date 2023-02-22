package net.gaox.job.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p> xxl-job config </p>
 *
 * @author gaox·Eric
 * @date 2023-02-21 23:12
 */

@Data
@Slf4j
@ConfigurationProperties(prefix = "xxl.job")
public class XxlJobConfig {

    private JobAdminProperties admin = new JobAdminProperties();

    private JobExecutorProperties executor = new JobExecutorProperties();

    /**
     * 任务执行器管理配置信息
     */
    @Data
    public static class JobAdminProperties {

        private String addresses;

        private String userName;

        private String password;

        private Integer connectionTimeOut = 5_000;

        private Boolean update = false;

    }

    /**
     * 任务执行器配置信息
     */
    @Data
    public static class JobExecutorProperties {

        private String accessToken;

        private String appName;

        private String title;

        private String address;

        private String ip;

        private int port;

        /**
         * 执行器地址类型：0=自动注册、1=手动录入
         */
        private String addressType;

        /**
         * 执行器地址列表，多地址逗号分隔(手动录入)
         */
        private String addressList;

        private String logPath;

        private int logRetentionDays;

    }

}