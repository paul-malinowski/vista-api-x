package gov.va.octo.vista.api.jwt;

import java.security.Principal;
import java.util.List;
import java.util.function.Predicate;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import gov.va.octo.vista.api.client.RpcRequestX;
import gov.va.octo.vista.api.enumeration.AppConfigTypeEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import lombok.extern.slf4j.Slf4j;

@Getter
@Builder
@Jacksonized
@JsonInclude(Include.NON_NULL)
@Slf4j
public final class JwtUserPrincipal implements Principal {

    private static final String WILDCARD = "*";
    private static final String DDR_PREFIX = "DDR";

    /**
     * token payload key: user.application Name of the application for given user authentication
     */
    private String application;

    /**
     * token payload key: user.domain domain of the given user authentication
     */
    private String domain;

    /**
     * token payload key: user.id unique identifier of the user from the authentication service
     * 
     * may or may not have any meaning/relevance to the consuming application/service
     */
    private String id;

    /**
     * token payload key: user.username username for the given user.
     * 
     * VistA authentication, this will most likely be a duplicate of the DUZ
     */
    private String username;

    /**
     * token payload key: user.duz DUZ of the user for VistA authentication
     * 
     * will be missing/blank/null for non-VistA authentication
     */
    private String duz;

    /**
     * token payload key: user.firstname
     */
    private final String firstName;

    /**
     * token payload key: user.lastname
     */
    private final String lastName;

    /**
     * token payload key: user.email
     */
    private final String email;

    /**
     * token payload key: user.service-account
     * 
     * boolean flag used to identify service accounts.
     * 
     * in WSC/SSC environments this setting prevents the password from being changed by the end user
     * but may have other application specific ramifications at the client/service level
     */
    private final boolean serviceAccount;

    /**
     * token payload key: user.app-entry
     * 
     * string identifying an application entry point
     * 
     * in WSC/SSC environments this setting forwards the user directly to a specific application
     * module
     */
    private final String applicationEntry;

    /**
     * token payload key: user.phone
     */
    private final String phone;

    private final List<String> flags;

    /**
     * token payload key: user.authorities list of roles/permissions granted to the user
     */
    private final List<RpcPermission> authorities;

    @Override
    public String getName() {
        return username;
    }


    private final List<VistaId> vistaIds;


    private boolean ssoiToken;

    private String sessionId;

    private String secId;

    private String samAccountName;


    /**
     * assumes only one vistaId for given station; otherwise results may not be as expected
     * 
     * only checking for root station
     * 
     * 
     * @param  stationNo
     * @return
     */
    public VistaId getVista(String stationNo) {
        if (vistaIds == null)
            return null;
        String sta3n = stationNo.substring(0, 3);
        log.debug("stationNo: " + stationNo + "  looking for: " + sta3n);
        return vistaIds.stream()
                .filter(v -> StringUtils.compare(v.getSiteId(), sta3n) == 0)
                .findFirst()
                .orElse(null);
    }


    public VistaId getVista(String stationNo, String duz) {
        if (vistaIds == null)
            return null;
        String sta3n = stationNo.substring(0, 3);
        log.debug("stationNo: " + stationNo + "  looking for: " + sta3n);
        return vistaIds.stream()
                .filter(v -> StringUtils.compare(v.getSiteId(), sta3n) == 0
                        && StringUtils.compare(v.getDuz(), duz) == 0)
                .findFirst()
                .orElse(null);
    }


    public boolean hasVistaMapping(String stationNo) {
        if (vistaIds == null)
            return false;
        return getVista(stationNo) == null ? false : true;
    }


    public boolean hasVistaMapping(String stationNo, String duz) {
        if (vistaIds == null)
            return false;
        return getVista(stationNo, duz) == null ? false : true;
    }



    public boolean allowConnection(@NotNull String stationNo, @NotNull String duz) {

        Predicate<VistaId> matchStation =
                x -> (x.getSiteId().toUpperCase().equals(stationNo.toUpperCase()));
        Predicate<VistaId> matchDuz = x -> (x.getDuz().toUpperCase().equals(duz.toUpperCase()));
        Predicate<VistaId> wildcardStation = x -> (x.getSiteId().equals(WILDCARD));
        Predicate<VistaId> wildcardDuz = x -> (x.getDuz().equals(WILDCARD));

        Predicate<VistaId> unlimited = wildcardStation.and(wildcardDuz);
        Predicate<VistaId> anyDuz = matchStation.and(wildcardDuz);
        Predicate<VistaId> fullmatch = matchStation.and(matchDuz);


        if (vistaIds == null || vistaIds.isEmpty())
            return false;

        return vistaIds.stream().anyMatch(unlimited.or(anyDuz).or(fullmatch));

    }

    public boolean allowExecution(@NotNull RpcRequestX req) {

        return allowExecution(req.getContext(), req.getRpc());

    }

    public boolean hasFlag(@NotNull AppConfigTypeEnum config) {
        if (flags == null) {
            return false;
        }

        for (String f : flags) {
            if (f.equals(config.getName())) {
                return true;
            }
        }

        return false;
    }

    public boolean allowExecution(@NotNull String context, @NotNull String rpc) {

        // if rpc start with DDR then only allow execution if the ALLOW_DDR flag exists
        // at the point there are only 10 RPCs in VistA that will be disallowed but
        // they can be in any context. the present some security risks because arbitrary
        // M code can be run with them.

        if (rpc.startsWith(DDR_PREFIX)) {
            if (!hasFlag(AppConfigTypeEnum.ALLOW_DDR)) {
                log.warn("unauthorized DDR rpc attempted by app: " + username);
                return false;
            }
        }

        Predicate<RpcPermission> matchContext =
                x -> (x.getContext().toUpperCase().equals(context.toUpperCase()));
        Predicate<RpcPermission> matchRpc =
                x -> (x.getRpc().toUpperCase().equals(rpc.toUpperCase()));
        Predicate<RpcPermission> wildcardContext = x -> (x.getContext().equals(WILDCARD));
        Predicate<RpcPermission> wildcardRpc = x -> (x.getRpc().equals(WILDCARD));

        Predicate<RpcPermission> unlimited = wildcardContext.and(wildcardRpc);
        Predicate<RpcPermission> allRpcsInContext = matchContext.and(wildcardRpc);
        Predicate<RpcPermission> fullmatch = matchContext.and(matchRpc);


        if (authorities == null || authorities.isEmpty())
            return false;

        return authorities.stream().anyMatch(unlimited.or(allRpcsInContext).or(fullmatch));

    }


}
