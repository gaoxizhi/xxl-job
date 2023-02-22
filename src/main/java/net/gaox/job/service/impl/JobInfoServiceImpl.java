package net.gaox.job.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import net.gaox.job.config.XxlJobConfig;
import net.gaox.job.model.XxlJobInfo;
import net.gaox.job.service.JobInfoService;
import net.gaox.job.service.JobLoginService;
import net.gaox.job.util.Constant;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * <p> 任务维护实现 </p>
 *
 * @author gaox·Eric
 * @date 2023-02-21 22:02
 */

@Service
@RequiredArgsConstructor
public class JobInfoServiceImpl implements JobInfoService {

    private final XxlJobConfig xxlJobConfig;

    private final JobLoginService jobLoginService;

    @Override
    public List<XxlJobInfo> getJobInfo(Integer jobGroupId, String executorHandler) {
        String url = xxlJobConfig.getAdmin().getAddresses() + Constant.JOB_LIST;
        HttpResponse response = HttpRequest.post(url)
                .form("jobGroup", jobGroupId)
                .form("executorHandler", executorHandler)
                .form("triggerStatus", -1)
                .cookie(jobLoginService.getCookie())
                .execute();

        String body = response.body();
        JSONArray array = JSONUtil.parse(body).getByPath("data", JSONArray.class);
        List<XxlJobInfo> list = array.stream()
                .map(o -> JSONUtil.toBean((JSONObject) o, XxlJobInfo.class))
                .collect(Collectors.toList());

        return list;
    }

    @Override
    public Integer addJobInfo(XxlJobInfo xxlJobInfo) {
        String url = xxlJobConfig.getAdmin().getAddresses() + Constant.JOB_SAVE;
        Map<String, Object> paramMap = BeanUtil.beanToMap(xxlJobInfo);
        HttpResponse response = HttpRequest.post(url)
                .form(paramMap)
                .cookie(jobLoginService.getCookie())
                .execute();

        JSON json = JSONUtil.parse(response.body());
        Object code = json.getByPath(Constant.CODE);
        if (Objects.equals(code,HttpStatus.HTTP_OK)) {
            return Convert.toInt(json.getByPath("content"));
        }
        throw new RuntimeException("add jobInfo error!");
    }

    @Override
    public Integer updateJobInfo(XxlJobInfo xxlJobInfo) {
        String url = xxlJobConfig.getAdmin().getAddresses() + Constant.JOB_UPDATE;
        Map<String, Object> paramMap = BeanUtil.beanToMap(xxlJobInfo);
        HttpResponse response = HttpRequest.post(url)
                .form(paramMap)
                .cookie(jobLoginService.getCookie())
                .execute();

        JSON json = JSONUtil.parse(response.body());
        Object code = json.getByPath(Constant.CODE);
        if (Objects.equals(code,HttpStatus.HTTP_OK)) {
            return Convert.toInt(json.getByPath(Constant.CONTENT));
        }
        throw new RuntimeException("update jobInfo error!");
    }

}
