package net.gaox.job.schedule;

import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import net.gaox.job.annotation.XxlRegister;
import org.springframework.stereotype.Component;

/**
 * <p> 测试入口 </p>
 *
 * @author gaox·Eric
 * @date 2023-02-22 01:18
 */
@Slf4j
@Component
public class GameBeat {

    @XxlJob("csgo")
    @XxlRegister(cron = "0 * * * * ?", jobDesc = "csgo 入口", author = "gaox",
            executorRouteStrategy = "RANDOM", triggerStatus = 1)
    public void csgo() {
        log.warn("csgo 调度成功！");
    }

    @XxlJob("cf")
    @XxlRegister(cron = "0,30 * * * * ?", jobDesc = "cf 入口", author = "gaox", triggerStatus = 1)
    public void cf() {
        log.warn("cf 调度成功！");
    }

    @XxlJob("dnf")
    @XxlRegister(cron = "0/3 * * * * ?", jobDesc = "dnf 入口", author = "gaox", triggerStatus = 1)
    public void dnf() {
        log.warn("dnf 调度成功！");
    }
}
