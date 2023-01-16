package pl.com.segware.gapa.second.configs;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .mvcMatchers("/health", "/metrics", "/prometheus").permitAll()
                .and().oauth2ResourceServer().opaqueToken();
    }

    @Bean
    public OpaqueTokenIntrospector tokenIntrospector(OAuth2ResourceServerProperties resourceServerProps) {
        return new CustomOpaqueTokenIntrospector(resourceServerProps);
    }
}
