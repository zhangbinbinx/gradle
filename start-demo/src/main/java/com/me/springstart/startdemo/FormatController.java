package com.me.springstart.startdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.me.springbootstarter.demo.starter.format.IFormatProcess;
/**
 * @description: 格式化控制类
 * @author: zhangbinbin
 * @create: 2019-07-23 20:58
 **/
@RestController
public class FormatController {
    @Autowired
    IFormatProcess formatProcess;
    @GetMapping("/format.do")
    public <T>String format(T t){
        return formatProcess.format(t);
    }
}
