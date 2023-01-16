package pl.com.segeware.gapagateway.configs;

import org.springframework.security.crypto.keygen.Base64StringKeyGenerator;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.server.DefaultServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.PkceParameterNames;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class PkceAuthorizationRequestResolver implements ServerOAuth2AuthorizationRequestResolver {
    private ServerOAuth2AuthorizationRequestResolver defaultResolver;
    private final StringKeyGenerator secureKeyGenerator = new Base64StringKeyGenerator(Base64.getUrlEncoder().withoutPadding(), 96);

    public PkceAuthorizationRequestResolver(ReactiveClientRegistrationRepository repo) {
        defaultResolver = new DefaultServerOAuth2AuthorizationRequestResolver(repo);
    }

    @Override
    public Mono<OAuth2AuthorizationRequest> resolve(ServerWebExchange exchange) {
        Mono<OAuth2AuthorizationRequest> req = defaultResolver.resolve(exchange);
        return customizeAuthorizationRequest(req);
    }

    @Override
    public Mono<OAuth2AuthorizationRequest> resolve(ServerWebExchange exchange, String clientRegistrationId) {
        Mono<OAuth2AuthorizationRequest> req = defaultResolver.resolve(exchange, clientRegistrationId);
        return customizeAuthorizationRequest(req);
    }

    private Mono<OAuth2AuthorizationRequest> customizeAuthorizationRequest(Mono<OAuth2AuthorizationRequest> req) {
        return req.map(original -> {
            Map<String, Object> attributes = new HashMap<>(original.getAttributes());
            Map<String, Object> additionalParameters = new HashMap<>(original.getAdditionalParameters());
            addPkceParameters(attributes, additionalParameters);
            return OAuth2AuthorizationRequest.from(original)
                    .attributes(attributes)
                    .additionalParameters(additionalParameters)
                    .build();
        });
    }

    private void addPkceParameters(Map<String, Object> attributes, Map<String, Object> additionalParameters) {
        String codeVerifier = secureKeyGenerator.generateKey();
        attributes.put(PkceParameterNames.CODE_VERIFIER, codeVerifier);
        try {
            String codeChallenge = createHash(codeVerifier);
            additionalParameters.put(PkceParameterNames.CODE_CHALLENGE, codeChallenge);
            additionalParameters.put(PkceParameterNames.CODE_CHALLENGE_METHOD, "S256");
        } catch (NoSuchAlgorithmException e) {
            additionalParameters.put(PkceParameterNames.CODE_CHALLENGE, codeVerifier);
        }
    }

    private static String createHash(String value) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(value.getBytes(StandardCharsets.US_ASCII));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
    }
}
