package com.shgx.provider.service;

import com.shgx.service.service.HelloService;
import org.springframework.stereotype.Component;
import com.alibaba.dubbo.config.annotation.Service;

/**
 * @author: guangxush
 * @create: 2020/01/02
 */
@Component
@Service(version = "1.0.0", timeout = 3000)
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return "Hello " + name;
    }
}
