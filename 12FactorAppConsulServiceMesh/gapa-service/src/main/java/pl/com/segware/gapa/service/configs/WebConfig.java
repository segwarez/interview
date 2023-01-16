package pl.com.segware.gapa.service.configs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PreDestroy;

@Slf4j
@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean getTokenFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new RequestFilter());
        registration.addUrlPatterns("/ping");
        registration.setName("requestFilter");
        return registration;
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .additionalInterceptors(new RestTemplateInterceptor())
                .build();
    }

    @PreDestroy
    void unloadRequestContext() {
        RequestContext.unload();
        log.info("RequestContext removed.");
    }
}
