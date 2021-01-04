package top.amfun.simple.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import top.amfun.simple.modules.ums.service.UmsAdminService;
import top.amfun.simple.security.config.SecurityConfig;

/**
 * @date 2020/10/28 17:19
 * @description:
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SystemSecurityConfig extends SecurityConfig {

    @Autowired
    private UmsAdminService umsAdminService;

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return username -> umsAdminService.loadUserByUsername(username);
    }
}
