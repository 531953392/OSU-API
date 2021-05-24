package com.lvb.baseApi.restful.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TemplatesController {


    @RequestMapping("home/tipsInfo")
    public String tipsInfo(){
        return "小程序升级中...";
    }
}
