package net.gaox.job.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import lombok.RequiredArgsConstructor;
import net.gaox.job.config.XxlJobConfig;
import net.gaox.job.service.JobLoginService;
import net.gaox.job.util.Constant;
import org.springframework.stereotype.Service;

import java.net.HttpCookie;
import java.util.*;

/**
 * <p> 登陆实现 </p>
 *
 * @author gaox·Eric
 * @date 2023-02-21 21:36
 */

@Service
@RequiredArgsConstructor
public class JobLoginServiceImpl implements JobLoginService {

    private final XxlJobConfig xxlJobConfig;

    private final Map<String, String> loginCookie = new HashMap<>();

    @Override
    public void login() {
        String url = xxlJobConfig.getAdmin().getAddresses() + Constant.LOGIN;
        HttpResponse response = HttpRequest.post(url)
                .form("userName", xxlJobConfig.getAdmin().getUserName())
                .form("password", xxlJobConfig.getAdmin().getPassword())
                .execute();
        List<HttpCookie> cookies = response.getCookies();
        Optional<HttpCookie> cookieOpt = cookies.stream()
                .filter(cookie -> Objects.equals(cookie.getName(),Constant.XXL_JOB_LOGIN_IDENTITY)).findFirst();
        if (!cookieOpt.isPresent()) {
            throw new RuntimeException("get xxl-job cookie error!");
        }

        String value = cookieOpt.get().getValue();
        loginCookie.put(Constant.XXL_JOB_LOGIN_IDENTITY, value);
    }

    @Override
    public String getCookie() {
        for (int i = 0; i < Constant.RETRY_TIMES; i++) {
            String cookieStr = loginCookie.get(Constant.XXL_JOB_LOGIN_IDENTITY);
            if (cookieStr != null) {
                return Constant.XXL_JOB_LOGIN_IDENTITY + "=" + cookieStr;
            }
            login();
        }
        throw new RuntimeException("get xxl-job cookie error!");
    }


}
