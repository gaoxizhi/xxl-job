package net.gaox.job.service;

import net.gaox.job.model.XxlJobInfo;

import java.util.List;

/**
 * <p> 任务 </p>
 *
 * @author gaox·Eric
 * @date 2023-02-21 21:32
 */

public interface JobInfoService {

    /**
     * 根据调度器id和任务名称 模糊查询任务列表
     *
     * @param jobGroupId      调度器id
     * @param executorHandler 任务名称
     * @return 任务列表
     */
    List<XxlJobInfo> getJobInfo(Integer jobGroupId, String executorHandler);

    /**
     * 新增任务
     *
     * @param xxlJobInfo 任务详情
     * @return 新任务id
     */
    Integer addJobInfo(XxlJobInfo xxlJobInfo);

    /**
     * 根据任务名称修改任务
     *
     * @param xxlJobInfo 任务详情
     * @return null
     */
    Integer updateJobInfo(XxlJobInfo xxlJobInfo);

}
