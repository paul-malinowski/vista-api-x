package gov.va.octo.vista.api.jwt;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

/**
 * Details describing a token
 * 
 * Used for creation and parsing
 *
 * @author william.mccarty@va.gov
 */
@Getter
@Builder
public final class JwtTokenDetails {

    private static final int DEFAULT_TTL = 3;

    @Builder.Default
    private final String id = UUID.randomUUID().toString();;

    private final PrivateKey privateKey;
    private final PublicKey publicKey;
    private final String issuer;
    private final String audience;
    private final String subject;
    private final String applicationKey; // application key

    @Builder.Default
    private final int refreshCount = 0; // number of times token has been refreshed

    @Builder.Default
    private final int ttl = DEFAULT_TTL; // time to live; minutes

    @Builder.Default
    private final int refreshTtl = 60; // maximum time (minutes) token can be refreshed
                                       // formal without credential check

    @Builder.Default
    private final ZonedDateTime issueDate = ZonedDateTime.now();

    @Builder.Default
    private final ZonedDateTime expirationDate = ZonedDateTime.now().plusMinutes(DEFAULT_TTL);

    /**
     * Check if the authentication token is eligible for refreshment.
     *
     * @return
     */
    public boolean isEligibleForRefresh() {
        ZonedDateTime maxTtl = issueDate.plusMinutes(refreshTtl);
        ZonedDateTime current = ZonedDateTime.now();
        return current.isBefore(maxTtl);
    }

    public static class JwtTokenDetailsBuilder {
        @SuppressWarnings("unused")
        private int ttl;
        @SuppressWarnings("unused")
        private ZonedDateTime expirationDate;

        public JwtTokenDetailsBuilder ttl(int ttl) {
            this.ttl = ttl;
            expirationDate = ZonedDateTime.now().plusMinutes(ttl);
            return this;
        }

        public JwtTokenDetailsBuilder expirationDate(ZonedDateTime expirationDate) {
            this.expirationDate = expirationDate;
            return this;
        }

    }

}
