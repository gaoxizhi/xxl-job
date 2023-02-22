package net.gaox.job.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p> 测试 </p>
 *
 * @author gaox·Eric
 * @date 2023-02-22 23:40
 */
@RequestMapping
@RestController
public class HelloController {

    @GetMapping
    public String hello() {

        return "hello from xxl-job test!";
    }
}
