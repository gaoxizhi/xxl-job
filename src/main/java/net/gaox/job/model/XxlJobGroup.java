package net.gaox.job.model;

import cn.hutool.core.annotation.Alias;
import lombok.Data;

import java.util.Date;

/**
 * <p> 调度器 </p>
 *
 * @author gaox·Eric
 * @date 2023-02-21 21:22
 */
@Data
public class XxlJobGroup {

    private int id;

    /**
     * 执行器AppName
     */
    @Alias("appname")
    private String appName;

    /**
     * 执行器名称
     */
    private String title;

    /**
     * 执行器地址类型：0=自动注册、1=手动录入
     */
    private int addressType;

    /**
     * 执行器地址列表，多地址逗号分隔(手动录入)
     */
    private String addressList;

    private Date updateTime;

}
