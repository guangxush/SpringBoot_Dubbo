package com.shgx.consumer.controller;

import com.shgx.service.HelloService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: guangxush
 * @create: 2020/01/02
 */
@RestController
public class HelloController {

    @Reference(version = "1.0.0", timeout = 3000)
    private HelloService helloService;

    @RequestMapping("/hello")
    public String hello() {
        String hello = helloService.sayHello("World");
        System.out.println(helloService.sayHello("Dubbo!!!"));
        return hello;
    }
}
