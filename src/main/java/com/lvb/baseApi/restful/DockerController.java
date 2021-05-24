package com.lvb.baseApi.restful;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class DockerController {

    @RequestMapping("/")
    public String index() {
        return "Hello Docker!";
    }

    @RequestMapping("/g6EKkPYJp2.txt")
    public String weixin_url() {
        return "d316b2495954c57cdd7f36ff167b3cd3";
    }
}
