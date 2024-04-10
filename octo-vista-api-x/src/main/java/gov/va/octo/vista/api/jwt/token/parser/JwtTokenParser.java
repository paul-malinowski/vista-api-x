package gov.va.octo.vista.api.jwt.token.parser;

import gov.va.octo.vista.api.jwt.JwtTokenDetails;
import gov.va.octo.vista.api.jwt.JwtUserPrincipal;

/**
 * Component which provides operations for parsing JWT tokens.
 *
 * @author william.mccarty@va.gov
 */
public interface JwtTokenParser {

    public boolean parseToken(String token);

    public JwtTokenDetails getJwtTokenDetails();

    public JwtUserPrincipal getJwtUserPrincipal();


}
