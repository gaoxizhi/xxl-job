package net.gaox.job.model;

import lombok.Data;

import java.util.Date;
import java.util.Objects;

/**
 * <p> 任务 </p>
 *
 * @author gaox·Eric
 * @date 2023-02-21 21:22
 */

@Data
public class XxlJobInfo {

    /**
     * 主键ID
     */
    private int id;

    /**
     * 执行器主键ID
     */
    private int jobGroup;
    private String jobDesc;
    private Date addTime;
    private Date updateTime;

    /**
     * 负责人
     */
    private String author;

    /**
     * 报警邮件
     */
    private String alarmEmail;

    /**
     * 调度类型
     */
    private String scheduleType;

    /**
     * 调度配置，值含义取决于调度类型
     */
    private String scheduleConf;

    /**
     * 调度过期策略
     */
    private String misfireStrategy;

    /**
     * 执行器路由策略
     */
    private String executorRouteStrategy;

    /**
     * 执行器，任务Handler名称
     */
    private String executorHandler;

    /**
     * 执行器，任务参数
     */
    private String executorParam;

    /**
     * 阻塞处理策略
     */
    private String executorBlockStrategy;

    /**
     * 任务执行超时时间，单位秒
     */
    private int executorTimeout;

    /**
     * 失败重试次数
     */
    private int executorFailRetryCount;

    /**
     * GLUE类型	#com.xxl.job.core.glue.GlueTypeEnum
     */
    private String glueType;

    /**
     * GLUE源代码
     */
    private String glueSource;

    /**
     * GLUE备注
     */
    private String glueRemark;

    /**
     * GLUE更新时间
     */
    private Date glueUpdateTime;

    /**
     * 子任务ID，多个逗号分隔
     */
    private String childJobId;

    /**
     * 调度状态：0-停止，1-运行
     */
    private int triggerStatus;

    /**
     * 上次调度时间
     */
    private long triggerLastTime;

    /**
     * 下次调度时间
     */
    private long triggerNextTime;

    /**
     * 比对任务
     *
     * @param info 任务详情
     * @return 任务是否一致
     */
    public Boolean equalByHandler(XxlJobInfo info) {

        Boolean equal = true;
        equal &= Objects.equals(jobDesc, info.getJobDesc());
        equal &= Objects.equals(author, info.getAuthor());
        equal &= Objects.equals(scheduleConf, info.getScheduleConf());
        equal &= Objects.equals(executorRouteStrategy, info.getExecutorRouteStrategy());
        equal &= Objects.equals(triggerStatus, info.getTriggerStatus());
        return equal;
    }

    /**
     * 同步最新任务详情
     *
     * @param info 最新任务详情
     */
    public void resetBy(XxlJobInfo info) {

        if (!Objects.equals(jobDesc, info.getJobDesc())) {
            this.jobDesc = info.getJobDesc();
        }
        if (!Objects.equals(author, info.getAuthor())) {
            this.author = info.getAuthor();
        }
        if (!Objects.equals(scheduleConf, info.getScheduleConf())) {
            this.scheduleConf = info.getScheduleConf();
        }
        if (!Objects.equals(executorRouteStrategy, info.getExecutorRouteStrategy())) {
            this.executorRouteStrategy = info.getExecutorRouteStrategy();
        }
        if (!Objects.equals(triggerStatus, info.getTriggerStatus())) {
            this.triggerStatus = info.getTriggerStatus();
        }
    }

}
