package net.gaox.job.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import net.gaox.job.config.XxlJobConfig;
import net.gaox.job.model.XxlJobGroup;
import net.gaox.job.service.JobGroupService;
import net.gaox.job.service.JobLoginService;
import net.gaox.job.util.Constant;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p> 调度器维护实现 </p>
 *
 * @author gaox·Eric
 * @date 2023-02-21 21:50
 */

@Service
@RequiredArgsConstructor
public class JobGroupServiceImpl implements JobGroupService {

    private final XxlJobConfig xxlJobConfig;
    private final JobLoginService jobLoginService;


    @Override
    public List<XxlJobGroup> getJobGroup() {

        String url = xxlJobConfig.getAdmin().getAddresses() + Constant.GROUP_LIST;
        HttpResponse response = HttpRequest.post(url)
                .form(Constant.APP_NAME, xxlJobConfig.getExecutor().getAppName())
                .form(Constant.TITLE, xxlJobConfig.getExecutor().getTitle())
                .cookie(jobLoginService.getCookie())
                .execute();

        String body = response.body();
        JSONArray array = JSONUtil.parse(body).getByPath("data", JSONArray.class);
        List<XxlJobGroup> list = array.stream()
                .map(o -> JSONUtil.toBean((JSONObject) o, XxlJobGroup.class))
                .collect(Collectors.toList());

        return list;
    }

    @Override
    public Boolean autoRegisterGroup() {
        String url = xxlJobConfig.getAdmin().getAddresses() + Constant.GROUP_SAVE;
        String title = xxlJobConfig.getExecutor().getTitle();
        String appName = xxlJobConfig.getExecutor().getAppName();
        String addressType = xxlJobConfig.getExecutor().getAddressType();
        String addressList = xxlJobConfig.getExecutor().getAddressList();

        HttpRequest httpRequest = HttpRequest.post(url)
                .form(Constant.APP_NAME, appName)
                .form(Constant.TITLE, title);

        httpRequest.form(Constant.ADDRESS_TYPE, addressType);
        if (Objects.equals(addressType, 1)) {
            if (Strings.isBlank(addressList)) {
                throw new RuntimeException("手动录入模式下，执行器地址列表不能为空");
            }
            httpRequest.form(Constant.ADDRESS_LIST, addressList);
        }

        HttpResponse response = httpRequest.cookie(jobLoginService.getCookie()).execute();
        Object code = JSONUtil.parse(response.body()).getByPath(Constant.CODE);
        return Objects.equals(code, HttpStatus.HTTP_OK);
    }

    @Override
    public Boolean preciselyCheck() {
        List<XxlJobGroup> jobGroup = getJobGroup();
        Optional<XxlJobGroup> has = jobGroup.stream()
                .filter(s -> Objects.equals(s.getAppName(), xxlJobConfig.getExecutor().getAppName()))
                .filter(s -> Objects.equals(s.getTitle(), xxlJobConfig.getExecutor().getTitle()))
                .findAny();
        return has.isPresent();
    }

}
