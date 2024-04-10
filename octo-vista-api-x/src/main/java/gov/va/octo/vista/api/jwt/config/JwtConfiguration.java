package gov.va.octo.vista.api.jwt.config;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import lombok.Getter;

@Dependent
@Getter
public class JwtConfiguration {

    /**
     * Application name
     */
    @Inject
    @JwtConfigurable("jwt.application.name")
    private String applicationName;

    /**
     * Application key
     */
    @Inject
    @JwtConfigurable("jwt.application.key")
    private String applicationKey;

    /**
     * TTL
     */
    @Inject
    @JwtConfigurable("jwt.ttl")
    private int ttl;

    /**
     * refresh ttl
     */
    @Inject
    @JwtConfigurable("jwt.refreshttl")
    private int refreshTtl;

    /**
     * Secret for signing token
     */
    @Inject
    @JwtConfigurable("jwt.private.key")
    private String privateKey;


    /**
     * Secret for verifying the token signature.
     */
    @Inject
    @JwtConfigurable("jwt.public.key")
    private String publicKey;

    /**
     * Identifies the recipients that the JWT token is intended for.
     */
    @Inject
    @JwtConfigurable("jwt.audience")
    private String audience;

    /**
     * Identifies the JWT token issuer.
     */
    @Inject
    @JwtConfigurable("jwt.issuer")
    private String issuer;

    /**
     * Allowed clock skew for verifying the token signature (in seconds).
     */
    @Inject
    @JwtConfigurable("jwt.clockSkew")
    private Long clockSkew;

    /**
     * SSOi Public Key
     */
    @Inject
    @JwtConfigurable("jwt.ssoi.public.key")
    private String ssoiPublicKey;



}
