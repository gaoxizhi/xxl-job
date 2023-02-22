package net.gaox.job.config;

import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.gaox.job.annotation.XxlRegister;
import net.gaox.job.model.XxlJobGroup;
import net.gaox.job.model.XxlJobInfo;
import net.gaox.job.service.JobGroupService;
import net.gaox.job.service.JobInfoService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * <p> Xxl 自动扫描注册执行器和任务 </p>
 *
 * @author gaox·Eric
 * @date 2023-02-21 22:11
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnExpression("#{'true'.equals(environment['xxl.job.admin.update'])}")
public class XxlJobAutoRegister implements ApplicationListener<ApplicationReadyEvent>, ApplicationContextAware {

    @Setter
    private ApplicationContext applicationContext;

    private final JobGroupService jobGroupService;

    private final JobInfoService jobInfoService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // 注册执行器
        addJobGroup();
        // 注册任务
        addJobInfo();
    }

    /**
     * 自动注册执行器
     */
    private void addJobGroup() {
        if (jobGroupService.preciselyCheck()) {
            return;
        }

        if (jobGroupService.autoRegisterGroup()) {
            log.info("auto register xxl-job group success!");
        }
    }

    /**
     * 自动注册任务
     */
    private void addJobInfo() {
        List<XxlJobGroup> jobGroups = jobGroupService.getJobGroup();
        XxlJobGroup xxlJobGroup = jobGroups.get(0);

        String[] beanDefinitionNames = applicationContext.getBeanNamesForType(Object.class, false, true);
        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = applicationContext.getBean(beanDefinitionName);

            Map<Method, XxlJob> annotatedMethods = MethodIntrospector.selectMethods(bean.getClass(),
                    (MethodIntrospector.MetadataLookup<XxlJob>) method -> AnnotatedElementUtils.findMergedAnnotation(method, XxlJob.class));
            for (Map.Entry<Method, XxlJob> methodXxlJobEntry : annotatedMethods.entrySet()) {
                Method executeMethod = methodXxlJobEntry.getKey();
                XxlJob xxlJob = methodXxlJobEntry.getValue();

                // 自动注册
                if (executeMethod.isAnnotationPresent(XxlRegister.class)) {
                    XxlRegister xxlRegister = executeMethod.getAnnotation(XxlRegister.class);
                    XxlJobInfo xxlJobInfo = createXxlJobInfo(xxlJobGroup, xxlJob, xxlRegister);

                    List<XxlJobInfo> jobInfo = jobInfoService.getJobInfo(xxlJobGroup.getId(), xxlJob.value());
                    if (!jobInfo.isEmpty()) {
                        // 因为是模糊查询，需要再判断一次
                        Optional<XxlJobInfo> first = jobInfo.stream()
                                .filter(s -> Objects.equals(s.getExecutorHandler(), xxlJob.value()))
                                .findFirst();
                        if (first.isPresent()) {
                            XxlJobInfo find = first.get();
                            if (xxlJobInfo.equalByHandler(find)) {
                                continue;
                            }
                            /// 开启、关闭任务实现
                            // find.resetBy(xxlJobInfo);
                            // jobInfoService.updateJobInfo(find);
                            // log.warn("更新任务 id = [{}], 任务详情[{}]", find.getId(), find);
                            // continue;
                        }
                    }

                    Integer jobInfoId = jobInfoService.addJobInfo(xxlJobInfo);
                    log.warn("新增任务 id = [{}], 任务详情[{}]", jobInfoId, jobInfo);
                }
            }
        }
    }

    /**
     * 构建任务info
     *
     * @param xxlJobGroup 执行器
     * @param xxlJob      xxl任务
     * @param xxlRegister 增强任务详情
     * @return jobInfo
     */
    private XxlJobInfo createXxlJobInfo(XxlJobGroup xxlJobGroup, XxlJob xxlJob, XxlRegister xxlRegister) {
        XxlJobInfo xxlJobInfo = new XxlJobInfo();
        xxlJobInfo.setJobGroup(xxlJobGroup.getId());
        xxlJobInfo.setJobDesc(xxlRegister.jobDesc());
        xxlJobInfo.setAuthor(xxlRegister.author());
        xxlJobInfo.setScheduleType("CRON");
        xxlJobInfo.setScheduleConf(xxlRegister.cron());
        xxlJobInfo.setGlueType("BEAN");
        xxlJobInfo.setExecutorHandler(xxlJob.value());
        xxlJobInfo.setExecutorRouteStrategy(xxlRegister.executorRouteStrategy());
        xxlJobInfo.setMisfireStrategy("DO_NOTHING");
        xxlJobInfo.setExecutorBlockStrategy("SERIAL_EXECUTION");
        xxlJobInfo.setExecutorTimeout(0);
        xxlJobInfo.setExecutorFailRetryCount(0);
        xxlJobInfo.setGlueRemark("GLUE代码初始化");
        xxlJobInfo.setTriggerStatus(xxlRegister.triggerStatus());

        return xxlJobInfo;
    }

}
