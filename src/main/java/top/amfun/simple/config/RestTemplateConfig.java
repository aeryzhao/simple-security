package top.amfun.simple.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author zhaoxg
 * @date 2020/11/23 10:20
 */
@Configuration
public class RestTemplateConfig {

    /**
     * 基于OkHttp3的RestTemplate
     * @return RestTemplate
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(okHttp3ClientHttpRequestFactory());
    }

/*    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        //默认的是JDK提供http连接，需要的话可以通过setRequestFactory方法替换为例如Apache HttpComponents、Netty或OkHttp等其它HTTP library。
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        //单位为ms
        factory.setReadTimeout(5000);
        //单位为ms
        factory.setConnectTimeout(5000);
        return factory;
    }*/

    @Bean
    public OkHttp3ClientHttpRequestFactory okHttp3ClientHttpRequestFactory() {
        OkHttp3ClientHttpRequestFactory factory = new OkHttp3ClientHttpRequestFactory();
        // 30秒
        factory.setConnectTimeout(30000);
        factory.setReadTimeout(30000);
        factory.setWriteTimeout(30000);
        return factory;
    }
}
