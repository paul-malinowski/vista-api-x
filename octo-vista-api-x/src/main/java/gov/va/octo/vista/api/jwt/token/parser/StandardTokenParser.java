package gov.va.octo.vista.api.jwt.token.parser;

import java.security.PublicKey;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import gov.va.octo.vista.api.jwt.JwtException;
import gov.va.octo.vista.api.jwt.JwtTokenClaims;
import gov.va.octo.vista.api.jwt.JwtTokenDetails;
import gov.va.octo.vista.api.jwt.config.JwtConfiguration;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.InvalidClaimException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;

/**
 * Component which provides operations for parsing JWT tokens.
 *
 * @author william.mccarty@va.gov
 */
@Dependent
@Slf4j
public class StandardTokenParser extends AbstractTokenParser implements JwtTokenParser {

    @Inject
    private JwtConfiguration settings;

    public boolean parseToken(String token) {

        try {

            PublicKey key = KeyUtils.publicKey(settings.getPublicKey());

            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .requireIssuer(settings.getIssuer())
                    .requireAudience(settings.getAudience())
                    .setAllowedClockSkewSeconds(settings.getClockSkew())
                    .parseClaimsJws(token)
                    .getBody();

            jwtTokenDetails = JwtTokenDetails.builder()
                    .id(extractFromClaims(claims, Claims.ID))
                    .issueDate(extractDateFromClaims(claims, Claims.ISSUED_AT))
                    .audience(extractFromClaims(claims, Claims.AUDIENCE))
                    .issuer(extractFromClaims(claims, Claims.ISSUER))
                    .subject(extractFromClaims(claims, Claims.SUBJECT))
                    .ttl(extractIntFromClaims(claims, JwtTokenClaims.TTL))
                    .expirationDate(extractDateFromClaims(claims, Claims.EXPIRATION))
                    .refreshCount(extractIntFromClaims(claims, JwtTokenClaims.REFRESH_COUNT))
                    .refreshTtl(extractIntFromClaims(claims, JwtTokenClaims.REFRESH_TTL))
                    .applicationKey(extractFromClaims(claims, JwtTokenClaims.APPLICATION_KEY))
                    .build();

            jwtUserPrincipal = extractUserFromClaims(claims);

            return true;

        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException e) {
            log.debug("unsupported/malformed token");
            throw new JwtException.InvalidTokenException("Invalid token");
        } catch (ExpiredJwtException e) {
            log.debug("expired token");
            throw new JwtException.InvalidTokenException("Expired token");
        } catch (InvalidClaimException e) {
            throw new JwtException.InvalidTokenException(
                    "Invalid value for claim \"" + e.getClaimName() + "\"");
        } catch (Exception e) {
            log.error("error parsing token", e);
            throw new JwtException.InvalidTokenException("Invalid token", e);
        }

    }

}
