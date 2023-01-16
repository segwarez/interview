package pl.com.segware.gapa.crud.configs;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and().authorizeRequests()
                .mvcMatchers("/health", "/metrics", "/prometheus").permitAll()
                .mvcMatchers("/v3/api-docs").authenticated()
                .mvcMatchers(HttpMethod.POST, "/users").hasAuthority("SCOPE_gapa:edit")
                .mvcMatchers(HttpMethod.PUT, "/users/**").hasAuthority("SCOPE_gapa:edit")
                .mvcMatchers(HttpMethod.DELETE, "/users/**").hasAuthority("SCOPE_gapa:edit")
                .mvcMatchers(HttpMethod.PATCH, "/users/**").hasAuthority("SCOPE_gapa:edit")
                .and().oauth2ResourceServer().opaqueToken();
    }

    @Bean
    OpaqueTokenIntrospector tokenIntrospector(OAuth2ResourceServerProperties resourceServerProps) {
        return new CustomOpaqueTokenIntrospector(resourceServerProps);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cors = new CorsConfiguration();
        cors.setAllowedMethods(Arrays.asList(
                HttpMethod.HEAD.name(),
                HttpMethod.POST.name(),
                HttpMethod.GET.name(),
                HttpMethod.DELETE.name(),
                HttpMethod.PUT.name(),
                HttpMethod.PATCH.name()));
        cors.applyPermitDefaultValues();
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);
        return source;
    }
}
