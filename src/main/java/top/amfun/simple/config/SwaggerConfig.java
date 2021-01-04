package top.amfun.simple.config;

import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import top.amfun.simple.common.config.BaseSwaggerConfig;
import top.amfun.simple.common.property.SwaggerProperties;

/**
 * @date 2020/10/27 16:25
 * @description: Swagger配置
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig extends BaseSwaggerConfig {

    @Override
    public SwaggerProperties swaggerProperties() {
        return SwaggerProperties.builder()
                .apiBasePackage("top.amfun.simple")
                .title("simple security")
                .description("Api接口测试")
                .contactName("xgng")
                .version("0.0.1-SNAPSHOT")
                .enableSecurity(true)
                .build();
    }
}
