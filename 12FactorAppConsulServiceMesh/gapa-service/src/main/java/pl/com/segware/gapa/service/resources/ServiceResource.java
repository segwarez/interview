package pl.com.segware.gapa.service.resources;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ff4j.FF4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class ServiceResource {
    private static final String FEATURE_VERBOSE = "verbose";
    private final FF4j ff4j;
    private final RestTemplate restTemplate;

    @GetMapping("/ping")
//    @Retry(name = "pingRetrier")
    @CircuitBreaker(name = "pingBreaker")
    @Bulkhead(name = "pingBulkhead")
    @PreAuthorize("hasAuthority('SCOPE_gapa:ping')")
    @Operation(summary = "Ping another service", security = @SecurityRequirement(name = "OAuth2"))
    public ResponseEntity<String> ping() {
        log.info("Ping: start!");
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8082/pong", String.class);
        if (log.isDebugEnabled()) {
            log.debug("Pong: response status code={}", response.getStatusCode());
        }
        String responseBody = response.getBody();
//        if (ff4j.check(FEATURE_VERBOSE)) {
//            responseBody = responseBody + " ping";
//        }
        return ResponseEntity.ok("Response from second: " + responseBody);
    }

    @GetMapping("/endpoints")
    @PreAuthorize("hasAuthority('SCOPE_gapa:read')")
    @Operation(summary = "View available endpoints", security = @SecurityRequirement(name = "OAuth2"))
    public ResponseEntity<String> service(@AuthenticationPrincipal Jwt jwt) {
        List<String> roles = jwt.getClaimAsStringList("roles");
        return ResponseEntity.ok(getServices(roles.contains("admin")));
    }

    private String getServices(boolean admin) {
        List<String> services = new ArrayList<>();
        services.add("http://localhost:8080/service/endpoints");
        if (admin) {
            services.add("http://localhost:8080/service/ping");
        }
        return services.toString();
    }
}
