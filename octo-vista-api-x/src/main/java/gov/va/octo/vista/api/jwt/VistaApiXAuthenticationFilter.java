package gov.va.octo.vista.api.jwt;

import java.io.IOException;
import java.util.List;
import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;
import org.apache.commons.lang3.StringUtils;
import gov.va.octo.vista.api.dao.AuthAppDao;
import gov.va.octo.vista.api.jwt.token.parser.JwtTokenParser;
import gov.va.octo.vista.api.jwt.token.parser.SsoiTokenParser;
import gov.va.octo.vista.api.jwt.token.parser.StandardTokenParser;
import gov.va.octo.vista.api.model.AuthApp;


@Provider
@Priority(Priorities.AUTHENTICATION)
public class VistaApiXAuthenticationFilter implements ContainerRequestFilter {

    private static final String HEADER_MAGIC_KEY = "X-OCTO-VISTA-API";

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String HEADER_AUTH = "X-UAAS-AUTH";
    private static final String AUTH_REQUEST = "auth-request";

    @Inject
    private SsoiTokenParser ssoiTokenParser;

    @Inject
    private StandardTokenParser standardTokenParser;

    @Inject
    private AuthAppDao authAppDao;


    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        // if the auth header exists; ignore the authorization header
        String authHeader = requestContext.getHeaderString(HEADER_AUTH);
        if (authHeader != null && AUTH_REQUEST.equals(authHeader)) {
            return;
        }

        // if the magic key header exists then we expect this the
        // auth header to contain an ssoi jwt; process accordingly
        String mk = requestContext.getHeaderString(HEADER_MAGIC_KEY);
        String auth = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (StringUtils.isBlank(auth) || !StringUtils.startsWith(auth, BEARER_PREFIX)) {
            return;
        }

        String token = auth.substring(BEARER_PREFIX.length());

        JwtTokenParser parser = deriveParser(mk);
        parser.parseToken(token); // exception thrown if parsing/validation fails

        boolean isHttps = requestContext.getSecurityContext().isSecure();
        javax.ws.rs.core.SecurityContext securityContext = new JwtSecurityContext(parser, isHttps);
        requestContext.setSecurityContext(securityContext);

    }



    private JwtTokenParser deriveParser(String magicKey) {

        // if there is no magic key header then we should
        // have a standard vista-api token
        if (StringUtils.isBlank(magicKey))
            return standardTokenParser;

        ssoiTokenParser.setAuthorities(buildAuthorities(magicKey));

        return ssoiTokenParser;

    }


    private List<RpcPermission> buildAuthorities(String magicKey) {

        AuthApp user = authAppDao.findByKey(magicKey);

        if (user == null || user.getPermissions() == null) {
            return List.of();
        }

        return user.getPermissions()
                .stream()
                .map(x -> new RpcPermission(x.getContext(), x.getRpc()))
                .toList();

    }


}
