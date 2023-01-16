package pl.com.segware.gapa.second;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@SecurityScheme(
        name = "OAuth2",
        in = SecuritySchemeIn.HEADER,
        type = SecuritySchemeType.OAUTH2,
        flows = @OAuthFlows(authorizationCode =
        @OAuthFlow(
                authorizationUrl = "http://localhost:8085/auth/realms/Gapa/protocol/openid-connect/auth",
                tokenUrl = "http://localhost:8085/auth/realms/Gapa/protocol/openid-connect/token"))
)
@OpenAPIDefinition(info =
@Info(title = "Gapa-Second API", version = "1.33.7", description = "Dokumentacja API", contact =
@Contact(name = "Kamil Sajdok", email = "kamil.sajdok@segware.com.pl")))
public class SecondApplication {
    public static void main(String[] args) {
        SpringApplication.run(SecondApplication.class, args);
    }
}
