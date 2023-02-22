package net.gaox.job.service;

import net.gaox.job.model.XxlJobGroup;

import java.util.List;

/**
 * <p> 调度器 </p>
 *
 * @author gaox·Eric
 * @date 2023-02-21 19:25
 */

public interface JobGroupService {

    /**
     * 获取调度器列表
     *
     * @return 调度器列表
     */
    List<XxlJobGroup> getJobGroup();

    /**
     * 注册调度器
     *
     * @return 注册状态
     */
    Boolean autoRegisterGroup();

    /**
     * 核对调度器，匹配appName和标题
     *
     * @return 是否完全一致
     */
    Boolean preciselyCheck();

}
