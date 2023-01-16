package pl.com.segware.gapa.crud.configs;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.NimbusOpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

public class CustomOpaqueTokenIntrospector implements OpaqueTokenIntrospector {
    private final OpaqueTokenIntrospector introspector;

    public CustomOpaqueTokenIntrospector(OAuth2ResourceServerProperties resourceServerProps) {
        OAuth2ResourceServerProperties.Opaquetoken opaqueTokenProps = resourceServerProps.getOpaquetoken();
        this.introspector = new NimbusOpaqueTokenIntrospector(
                opaqueTokenProps.getIntrospectionUri(),
                opaqueTokenProps.getClientId(),
                opaqueTokenProps.getClientSecret()
        );
    }

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        return introspector.introspect(token);
    }
}
