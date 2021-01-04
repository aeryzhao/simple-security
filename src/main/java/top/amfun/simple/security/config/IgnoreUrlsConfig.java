package top.amfun.simple.security.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @date 2020/10/27 10:54
 * @description: 配置SpringSecurity白名单
 */
@Getter
@Setter
@EnableConfigurationProperties(IgnoreUrlsConfig.class)
@ConfigurationProperties(prefix = "secure.ignored")
public class IgnoreUrlsConfig {
    private List<String> urls = new ArrayList<>();
}
