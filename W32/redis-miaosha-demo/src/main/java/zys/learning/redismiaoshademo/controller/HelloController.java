package zys.learning.redismiaoshademo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/index")
public class HelloController {

    @PostMapping("/hello")
    public String hello(String username) {
        System.out.println(username);
        return "helloworld";
    }
}
