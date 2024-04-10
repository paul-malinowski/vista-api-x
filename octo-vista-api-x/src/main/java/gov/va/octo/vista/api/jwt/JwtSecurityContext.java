package gov.va.octo.vista.api.jwt;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

import gov.va.octo.vista.api.jwt.token.parser.JwtTokenParser;
import lombok.Data;

/**
 * {@link SecurityContext} implementation for token-based authentication.
 *
 * @author william.mccarty@va.gov
 */
@Data
public class JwtSecurityContext implements SecurityContext {

    private JwtUserPrincipal jwtUserPrincipal;
    private JwtTokenDetails jwtTokenDetails;
    private final boolean secure;

    public JwtSecurityContext(JwtTokenDetails jwtTokenDetails, JwtUserPrincipal jwtUserDetails,
            boolean secure) {
        this.jwtUserPrincipal = jwtUserDetails;
        this.jwtTokenDetails = jwtTokenDetails;
        this.secure = secure;
    }

    public JwtSecurityContext(JwtTokenParser parser, boolean secure) {
        this.jwtUserPrincipal = parser.getJwtUserPrincipal();
        this.jwtTokenDetails = parser.getJwtTokenDetails();
        this.secure = secure;
    }

    @Override
    public Principal getUserPrincipal() {
        return jwtUserPrincipal;
    }

    @Override
    public boolean isUserInRole(final String role) {
        return jwtUserPrincipal.getAuthorities().contains(role);
    }

    @Override
    public boolean isSecure() {
        return secure;
    }

    @Override
    public String getAuthenticationScheme() {
        return "Bearer";
    }


}
