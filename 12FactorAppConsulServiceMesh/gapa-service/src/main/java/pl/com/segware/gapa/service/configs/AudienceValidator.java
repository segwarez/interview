package pl.com.segware.gapa.service.configs;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

public class AudienceValidator implements OAuth2TokenValidator<Jwt> {
    private static final OAuth2Error MISSING_AUDIENCE_ERROR = new OAuth2Error("invalid_token",
            "The required audience is missing", null);
    private final String audience;

    public AudienceValidator(String resource) {
        this.audience = resource;
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        boolean hasAudience = jwt.getAudience().contains(audience);
        if (hasAudience) {
            return OAuth2TokenValidatorResult.success();
        }
        return OAuth2TokenValidatorResult.failure(MISSING_AUDIENCE_ERROR);
    }
}
