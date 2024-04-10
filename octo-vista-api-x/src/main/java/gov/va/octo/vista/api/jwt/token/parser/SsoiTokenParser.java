package gov.va.octo.vista.api.jwt.token.parser;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.BadJWTException;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTClaimsVerifier;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import com.nimbusds.jwt.proc.JWTClaimsSetVerifier;
import gov.va.octo.vista.api.jwt.JwtException.InvalidTokenException;
import gov.va.octo.vista.api.jwt.JwtTokenClaims;
import gov.va.octo.vista.api.jwt.JwtUserPrincipal;
import gov.va.octo.vista.api.jwt.RpcPermission;
import gov.va.octo.vista.api.jwt.VistaId;
import gov.va.octo.vista.api.jwt.config.JwtConfiguration;
import lombok.extern.slf4j.Slf4j;

/**
 * Component which provides operations for parsing JWT tokens.
 *
 * @author william.mccarty@va.gov
 */
@Slf4j
@Dependent
public class SsoiTokenParser extends AbstractTokenParser implements JwtTokenParser {

    @Inject
    private JwtConfiguration settings;

    /**
     * not required if just validating token
     * 
     * set this if you need to use the resulting JwtUserPrincipal for later authorization
     * 
     */
    private List<RpcPermission> authorities;

    public boolean parseToken(String token) {

        try {

            ConfigurableJWTProcessor<SecurityContext> jwtProcessor =
                    new DefaultJWTProcessor<SecurityContext>();

            RSAKey rsaPublicKey =
                    new RSAKey.Builder(stringToKey(settings.getSsoiPublicKey())).build();
            JWKSet jwks = new JWKSet(rsaPublicKey);
            JWKSource<SecurityContext> keySource = new ImmutableJWKSet<SecurityContext>(jwks);
            JWSAlgorithm expectedJWSAlg = JWSAlgorithm.RS512;

            JWSKeySelector<SecurityContext> keySelector =
                    new JWSVerificationKeySelector<SecurityContext>(expectedJWSAlg, keySource);
            jwtProcessor.setJWSKeySelector(keySelector);

            JWTClaimsSetVerifier<SecurityContext> claimsVerifier =
                    new DefaultJWTClaimsVerifier<>(new JWTClaimsSet.Builder().build(),
                            new HashSet<>(Arrays.asList(JwtTokenClaims.USER_AUTHENTICATED,
                                    JwtTokenClaims.TOKEN_IDTYPE, JwtTokenClaims.TOKEN_EXP,
                                    JwtTokenClaims.SESSION_ID, JwtTokenClaims.TOKEN_NBF,
                                    JwtTokenClaims.USER_LAST_NAME, JwtTokenClaims.USER_FIRST_NAME,
                                    JwtTokenClaims.USER_EMAIL)));

            jwtProcessor.setJWTClaimsSetVerifier(claimsVerifier);

            SecurityContext ctx = null;
            JWTClaimsSet claimSet = jwtProcessor.process(token, ctx);

            if (!claimSet.getBooleanClaim(JwtTokenClaims.USER_AUTHENTICATED)) {
                throw new BadJWTException("authenticated = false");
            }

            if (!claimSet.getIssuer().startsWith("gov.va.vamf.userservice")
                    && !claimSet.getIssuer().startsWith("gov.va.mobile.sts")) {
                throw new BadJWTException("issuer:  invalid");
            }

            if (!claimSet.getStringClaim(JwtTokenClaims.TOKEN_IDTYPE).equals("secid")) {
                throw new BadJWTException(JwtTokenClaims.TOKEN_IDTYPE + ": invalid");
            }

            ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            Object vistaIdObj = claimSet.getClaim(JwtTokenClaims.USER_VISTA_IDS);
            List<VistaId> vistaIds = null;
            if (vistaIdObj != null) {
                String vistaIdStr = vistaIdObj.toString();
                vistaIds = Arrays.asList(mapper.readValue(vistaIdStr, VistaId[].class));
            } else {
                vistaIds = new ArrayList<VistaId>();
            }

            Map<String, Object> attributes =
                    claimSet.getJSONObjectClaim(JwtTokenClaims.USER_ATTRIBUTES);

            this.jwtUserPrincipal = JwtUserPrincipal.builder()
                    .sessionId(claimSet.getStringClaim(JwtTokenClaims.SESSION_ID))
                    .lastName(claimSet.getStringClaim(JwtTokenClaims.USER_LAST_NAME))
                    .firstName(claimSet.getStringClaim(JwtTokenClaims.USER_FIRST_NAME))
                    .secId(claimSet.getSubject())
                    .email(claimSet.getStringClaim(JwtTokenClaims.USER_EMAIL).toLowerCase())
                    .samAccountName(((String) attributes.get(JwtTokenClaims.USER_SAM_ACCOUNT_NAME))
                            .toLowerCase())
                    .vistaIds(vistaIds)
                    .authorities(authorities)
                    .ssoiToken(true)
                    .build();

            return true;

        } catch (JOSEException | BadJOSEException | JsonProcessingException | ParseException e) {
            log.warn("invalid token: " + e.getMessage());
            throw new InvalidTokenException("invalid token");
        }

    }

    private RSAPublicKey stringToKey(String str) {

        try {
            KeyFactory kFactory = KeyFactory.getInstance("RSA");
            // decode base64 of your key
            byte key[] = Base64.getDecoder().decode(str);
            // generate the public key
            X509EncodedKeySpec spec = new X509EncodedKeySpec(key);
            return (RSAPublicKey) kFactory.generatePublic(spec);

        } catch (Exception e) {
            log.error("exception creating pulbic key", e);
        }

        return null;

    }

    public List<RpcPermission> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<RpcPermission> authorities) {
        this.authorities = authorities;
    }

}
