package top.amfun.simple.modules.other.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.amfun.demo.mydemospringbootstarter.properties.DemoProperties;
import top.amfun.demo.mydemospringbootstarter.service.DemoService;

/**
 * @date 2020/11/6 11:44
 * @description:
 */
@Api(tags = "测试使用", description = "TestController")
@RestController
@RequestMapping("ignore/")
public class TestController {

    @Autowired
    private DemoService demoService;

    @Autowired
    private DemoProperties demoProperties;

    @ApiOperation(value = "测试调用自定义start")
    @GetMapping("/hi")
    public String testStarter() {
        return demoService.say();
    }

    @ApiOperation(value = "测试调用自定义start")
    @GetMapping("/properties")
    public String properties() {
        return demoProperties.getSayWhat() + demoProperties.getToWho();
    }
}
