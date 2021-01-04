package top.amfun.simple.modules.other.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.amfun.simple.util.OkHttpUtil;

/**
 * @author zhaoxg
 * @date 2020/11/17 15:49
 */

@RestController
@RequestMapping(value = "ignore/okhttp")
public class OkHttpController {

    @Autowired
    private OkHttpUtil okHttpUtil;

    @GetMapping("/showOk")
    public String show() {
        String url = "http://www.baidu.com";
        String message = okHttpUtil.doGet(url);
        return message;
    }
}
