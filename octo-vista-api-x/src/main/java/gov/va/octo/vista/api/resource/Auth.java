package gov.va.octo.vista.api.resource;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import gov.va.octo.vista.api.client.AuthenticationToken;
import gov.va.octo.vista.api.client.Credentials;
import gov.va.octo.vista.api.client.VistaApiResponse;
import gov.va.octo.vista.api.dao.AuthAppDao;
import gov.va.octo.vista.api.enumeration.AppConfigTypeEnum;
import gov.va.octo.vista.api.jwt.JwtException.AuthenticationException;
import gov.va.octo.vista.api.jwt.JwtTokenDetails;
import gov.va.octo.vista.api.jwt.JwtTokenIssuer;
import gov.va.octo.vista.api.jwt.JwtUserPrincipal;
import gov.va.octo.vista.api.jwt.RpcPermission;
import gov.va.octo.vista.api.jwt.VistaId;
import gov.va.octo.vista.api.jwt.config.JwtConfiguration;
import gov.va.octo.vista.api.jwt.token.parser.KeyUtils;
import gov.va.octo.vista.api.jwt.token.parser.SsoiTokenParser;
import gov.va.octo.vista.api.jwt.token.parser.StandardTokenParser;
import gov.va.octo.vista.api.model.AuthApp;
import gov.va.octo.vista.api.model.AuthAppConfig;
import gov.va.octo.vista.api.model.AuthStation;
import gov.va.octo.vista.api.utils.ResponseBuilder;
import lombok.extern.slf4j.Slf4j;

@Path("auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Slf4j
public class Auth extends BaseResource {

    @Inject
    private JwtConfiguration settings;

    @Inject
    private AuthAppDao authAppDao;

    @Inject
    private JwtTokenIssuer issuer;

    @Inject
    private StandardTokenParser standardTokenParser;

    @Inject
    private SsoiTokenParser ssoiTokenParser;


    private static String VERSION = "1.0";

    public String getVersion() {
        return VERSION;
    }

    @POST
    @PermitAll
    @Path("token")
    public VistaApiResponse<AuthenticationToken> token(Credentials cr) {


        // need to validate magic key
        AuthApp app = authAppDao.findByKey(cr.getKey());

        if (app == null || !app.isActive()) {
            String error = "invalid key";
            log.warn("authentication returned error: " + error);
            throw new AuthenticationException(error);
        }

        if (!app.hasConfig(AppConfigTypeEnum.ALLOW_VISTA_API_X_TOKEN)) {
            String error = "application key not valid for vista token usage";
            log.warn("authentication returned error: " + error);
            throw new AuthenticationException(error);
        }

        PrivateKey privateKey = null;
        PublicKey publicKey = null;
        try {
            privateKey = KeyUtils.privateKey(settings.getPrivateKey());
            publicKey = KeyUtils.publicKey(settings.getPublicKey());
        } catch (Exception e) {
            log.error("unable to create private or public key: unable to build token; aborting");
            log.error("exception", e);
            throw new AuthenticationException("configuration error with public/private key");
        }

        JwtTokenDetails jtd = JwtTokenDetails.builder()
                .applicationKey(settings.getApplicationKey())
                .audience(settings.getAudience())
                .issuer(settings.getIssuer())
                .privateKey(privateKey)
                .publicKey(publicKey)
                .ttl(settings.getTtl())
                .refreshTtl(settings.getRefreshTtl())
                .subject(app.getApp())
                .build();


        List<VistaId> vistaIds = new ArrayList<VistaId>();

        for (AuthStation st : app.getStations()) {
            VistaId vistaId = new VistaId(st.getStation(), st.getDuz(), "");
            vistaIds.add(vistaId);
        }

        List<String> flags = new ArrayList<>();

        for (AuthAppConfig c : app.getConfigs()) {
            flags.add(c.getConfig().getName());
        }

        JwtUserPrincipal jud = JwtUserPrincipal.builder()
                .ssoiToken(false)
                .username(app.getApp())
                .application(settings.getApplicationKey())
                .vistaIds(vistaIds)
                .authorities(buildPermissionList(app))
                .flags(flags)
                .build();

        String token = issuer.issueToken(jtd, jud);

        AuthenticationToken at = new AuthenticationToken();
        at.setToken(token);

        log.info("issue token {app: " + app.getApp() + ", application: "
                + settings.getApplicationName() + "}");

        return ResponseBuilder.build(at, this.uriInfo);
    }

    @POST
    @Path("refresh")
    @PermitAll
    public VistaApiResponse<AuthenticationToken> refreshToken(AuthenticationToken authToken) {

        String token = authToken.getToken();

        String privateKeyStr = settings.getPrivateKey();

        standardTokenParser.parseToken(token);

        // if we are here then the token is valid, attempt to refresh it
        PrivateKey privateKey = null;
        try {
            privateKey = KeyUtils.privateKey(privateKeyStr);
        } catch (Exception e) {
            log.error("unable to create private or public key: unable to build token; aborting");
            log.error("exception", e);
            throw new AuthenticationException("configuration error with public/private key");
        }

        String refreshedToken = issuer.refreshToken(standardTokenParser, privateKey);

        AuthenticationToken rat = new AuthenticationToken();
        rat.setToken(refreshedToken);

        log.info("refresh token {application: " + settings.getApplicationName() + "}");

        return ResponseBuilder.build(rat, this.uriInfo);
    }



    private List<RpcPermission> buildPermissionList(AuthApp tu) {

        List<RpcPermission> perms = new ArrayList<RpcPermission>();

        if (tu != null && tu.getPermissions() != null) {
            List<RpcPermission> ps = tu.getPermissions()
                    .stream()
                    .map(x -> new RpcPermission(x.getContext(), x.getRpc()))
                    .toList();
            perms.addAll(ps);
        }

        return perms;

    }



}
