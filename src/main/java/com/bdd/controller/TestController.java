package com.bdd.controller;

import com.bdd.component.LogComponent;
import com.bdd.pojo.ExceptionParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Description
 * @Author TenSong
 * @Date 2017/3/16 11:38
 */
@RestController
public class TestController {
    @Resource
    private LogComponent logComponent;

    @RequestMapping("/param")
    public ExceptionParam getString(){
        ExceptionParam param = new ExceptionParam();
        int i = 0;
        try {
            i = 100 / 0;
        } catch (Exception e) {
            param = logComponent.findExceptionParam(TestController.class, "运算错误", e);
        }

        return param;
    }

    @RequestMapping("/remove")
    public String remove(){
        logComponent.removeCache();
        return "Success";
    }
}
