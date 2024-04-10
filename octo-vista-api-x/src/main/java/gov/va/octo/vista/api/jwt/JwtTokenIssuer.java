package gov.va.octo.vista.api.jwt;

import java.security.PrivateKey;
import java.time.ZonedDateTime;
import java.util.Date;

import javax.enterprise.context.Dependent;

import gov.va.octo.vista.api.jwt.token.parser.JwtTokenParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

/**
 * Component which provides operations for issuing JWT tokens.
 *
 * @author william.mccarty@va.gov
 */

@Slf4j
@Dependent
public class JwtTokenIssuer {

    /**
     * Issue a JWT token
     *
     * @param  authenticationTokenDetails
     * @return
     */
    public String issueToken(JwtTokenDetails tokenDetails, JwtUserPrincipal userDetails) {
        return Jwts.builder()
                .setId(tokenDetails.getId())
                .setIssuer(tokenDetails.getIssuer())
                .setAudience(tokenDetails.getAudience())
                .setSubject(tokenDetails.getSubject())
                .setIssuedAt(Date.from(tokenDetails.getIssueDate().toInstant()))
                .setExpiration(Date.from(tokenDetails.getExpirationDate().toInstant()))
                .claim(JwtTokenClaims.REFRESH_COUNT, tokenDetails.getRefreshCount())
                .claim(JwtTokenClaims.TTL, tokenDetails.getTtl())
                .claim(JwtTokenClaims.REFRESH_TTL, tokenDetails.getRefreshTtl())
                .claim(JwtTokenClaims.USER, userDetails)
                .signWith(SignatureAlgorithm.RS256, tokenDetails.getPrivateKey())
                .compact();
    }


    /**
     * Refresh a token.
     *
     * @param  currentTokenDetails
     * @return
     */
    public String refreshToken(
            JwtTokenDetails ctd,
            JwtUserPrincipal userDetails,
            PrivateKey privateKey) {

        if (!ctd.isEligibleForRefresh()) {
            log.warn("token too old to be refreshed");
            throw new JwtException.TokenRefreshException("token too old to be refreshed");
        }

        int ttl = ctd.getTtl();
        ZonedDateTime expirationDate = ZonedDateTime.now().plusMinutes(ttl);

        JwtTokenDetails newTokenDetails = JwtTokenDetails.builder()
                .id(ctd.getId()) // reuse original id
                .issuer(ctd.getIssuer())
                .audience(ctd.getAudience())
                .subject(ctd.getSubject())
                .applicationKey(ctd.getApplicationKey())
                .refreshCount(ctd.getRefreshCount() + 1)
                .ttl(ctd.getTtl())
                .refreshTtl(ctd.getRefreshTtl())
                .issueDate(ctd.getIssueDate()) // reuse original issue date
                .expirationDate(expirationDate)
                .privateKey(privateKey)
                .build();

        return issueToken(newTokenDetails, userDetails);

    }


    public String refreshToken(JwtTokenParser parser, PrivateKey privateKey) {
        return refreshToken(parser.getJwtTokenDetails(), parser.getJwtUserPrincipal(), privateKey);
    }
}
