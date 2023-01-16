package pl.com.segware.gapa.second.resources;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ff4j.FF4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.segware.gapa.second.jms.KafkaProducer;

@Slf4j
@RestController
@AllArgsConstructor
@SecurityRequirement(name = "OAuth2")
public class SecondResource {
    private static final String FEATURE_VERBOSE = "verbose";
    private final FF4j ff4j;
    private final KafkaProducer kafkaProducer;

    @GetMapping("/pong")
    @PreAuthorize("hasAuthority('SCOPE_gapa:pong')")
    public ResponseEntity<String> pong() {
        log.info("Pong: Ping received!");
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String responseBody = "Scopes: " + authentication.getAuthorities();
//        if (ff4j.check(FEATURE_VERBOSE)) {
//            responseBody = responseBody + " pong";
//        }
        kafkaProducer.sendMessage("Pong");
        return ResponseEntity.ok(responseBody);
    }
}
