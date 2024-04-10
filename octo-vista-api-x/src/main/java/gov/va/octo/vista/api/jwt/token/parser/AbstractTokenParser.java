package gov.va.octo.vista.api.jwt.token.parser;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotNull;
import gov.va.octo.vista.api.jwt.JwtTokenClaims;
import gov.va.octo.vista.api.jwt.JwtTokenDetails;
import gov.va.octo.vista.api.jwt.JwtUserPrincipal;
import gov.va.octo.vista.api.jwt.RpcPermission;
import gov.va.octo.vista.api.jwt.VistaId;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;

/**
 * Component which provides operations for parsing JWT tokens.
 *
 * @author william.mccarty@va.gov
 */
@Slf4j
public abstract class AbstractTokenParser {

    protected JwtTokenDetails jwtTokenDetails;
    protected JwtUserPrincipal jwtUserPrincipal;


    /**
     * Extract a date from the token claims.
     *
     * @param  claims
     * @param  key
     * @return        date
     */
    protected ZonedDateTime extractDateFromClaims(@NotNull Claims claims, @NotNull String key) {
        long seconds = ((Number) claims.get(key)).longValue();
        long millis = seconds * 1000;
        Date d = new Date(millis);
        return ZonedDateTime.ofInstant(d.toInstant(), ZoneId.systemDefault());
    }


    /**
     * Extract an int from the token claims.
     *
     * @param  claims
     * @param  key
     * @return        Refresh count from the JWT token
     */
    protected int extractIntFromClaims(@NotNull Claims claims, @NotNull String key) {
        return (int) claims.get(key);
    }


    /**
     * extract a string from the token based on key
     * 
     * @param  claims
     * @param  key
     * @return
     */
    protected String extractFromClaims(@NotNull Claims claims, @NotNull String key) {
        return (String) claims.get(key);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public JwtUserPrincipal extractUserFromClaims(@NotNull Claims claims) {

        LinkedHashMap map = (LinkedHashMap) claims.get(JwtTokenClaims.USER);

        List<Object> vistaIdObj = (List<Object>) map.get(JwtTokenClaims.USER_VISTA_IDS);
        List<Object> authObj = (List<Object>) map.get(JwtTokenClaims.USER_AUTHORITIES);
        List<Object> flagObj = (List<Object>) map.get(JwtTokenClaims.ACCOUNT_FLAGS);
        List<VistaId> vistaIds = new ArrayList<>();
        List<RpcPermission> perms = new ArrayList<>();
        List<String> flags = new ArrayList<>();

        try {
            if (vistaIdObj != null && !vistaIdObj.isEmpty()) {
                vistaIds = vistaIdObj.stream()
                        .map(x -> (Map<String, String>) x)
                        .map(x -> new VistaId(x.get("siteId"), x.get("duz"), x.get("siteName")))
                        .toList();
            }

            if (authObj != null && !authObj.isEmpty()) {
                perms = authObj.stream()
                        .map(x -> (Map<String, String>) x)
                        .map(x -> new RpcPermission(x.get("context"), x.get("rpc")))
                        .toList();
            }

            if (flagObj != null && !flagObj.isEmpty()) {
                flags = flagObj.stream().map(x -> (String) x).toList();
            }


        } catch (Exception e) {
            log.error(e.getClass().getName(), e);
            vistaIds = new ArrayList<VistaId>();
        }

        JwtUserPrincipal jud = JwtUserPrincipal.builder()
                .application((String) map.get(JwtTokenClaims.USER_APPLICATION))
                .domain((String) map.get(JwtTokenClaims.USER_DOMAIN))
                .id((String) map.get(JwtTokenClaims.USER_ID))
                .username((String) map.get(JwtTokenClaims.USER_USERNAME))
                .duz((String) map.get(JwtTokenClaims.USER_DUZ))
                .firstName((String) map.get(JwtTokenClaims.USER_FIRST_NAME))
                .lastName((String) map.get(JwtTokenClaims.USER_LAST_NAME))
                .email((String) map.get(JwtTokenClaims.USER_EMAIL))
                .serviceAccount((boolean) map.get(JwtTokenClaims.USER_SERVICE_ACCOUNT))
                .applicationEntry((String) map.get(JwtTokenClaims.USER_APPLICATION_ENTRY))
                .phone((String) map.get(JwtTokenClaims.USER_PHONE))
                .authorities(perms)
                .vistaIds(vistaIds)
                .flags(flags)
                .build();

        return jud;
    }


    public JwtTokenDetails getJwtTokenDetails() {
        return jwtTokenDetails;
    }


    public JwtUserPrincipal getJwtUserPrincipal() {
        return jwtUserPrincipal;
    }


}
