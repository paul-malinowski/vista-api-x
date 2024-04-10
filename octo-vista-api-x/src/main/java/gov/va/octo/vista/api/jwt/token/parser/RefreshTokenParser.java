package gov.va.octo.vista.api.jwt.token.parser;

import java.security.PublicKey;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import gov.va.octo.vista.api.jwt.JwtException.InvalidTokenException;
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

/**
 * Component which provides operations for parsing JWT tokens.
 *
 * @author amccarty
 */
@Dependent
public class RefreshTokenParser extends AbstractTokenParser implements JwtTokenParser {

    @Inject
    private JwtConfiguration settings;

    @Override
    public boolean parseToken(String token) {
        return false;
    }

    public boolean parseToken(String token, String publicKey) {

        try {

            PublicKey key = KeyUtils.publicKey(publicKey);

            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .requireIssuer(settings.getIssuer())
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

        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException
                | SignatureException e) {
            throw new InvalidTokenException("Invalid token", e);
        } catch (ExpiredJwtException e) {
            throw new InvalidTokenException("Expired token", e);
        } catch (InvalidClaimException e) {
            throw new InvalidTokenException("Invalid value for claim \"" + e.getClaimName() + "\"",
                    e);
        } catch (Exception e) {
            throw new InvalidTokenException("Invalid token", e);
        }

    }

}
