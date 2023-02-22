package net.gaox.job.service;

/**
 * <p> 登陆管理控制台 </p>
 *
 * @author gaox·Eric
 * @date 2023-02-21 19:22
 */

public interface JobLoginService {

    /**
     * 登陆
     */
    void login();

    /**
     * 获取cookie
     *
     * @return cookie
     */
    String getCookie();

}
