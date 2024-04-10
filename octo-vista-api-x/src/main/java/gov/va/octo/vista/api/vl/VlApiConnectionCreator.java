package gov.va.octo.vista.api.vl;

import java.util.stream.Collectors;

import gov.va.octo.vista.api.jwt.JwtUserPrincipal;
import gov.va.octo.vista.api.jwt.VistaId;
import gov.va.octo.vistalink.VistaLinkUtils;
import gov.va.octo.vistalink.exception.InvalidStationMappingException;
import gov.va.octo.vistalink.exception.VistaLinkConnectionException;
import gov.va.octo.vistalink.model.VistaLinkConnectionX;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VlApiConnectionCreator {

    private static String MAPPING_ERR = "invalid station mapping";

    private static final int CONNECTION_ATTEMPTS = 10;
    private static final int WAIT_BETWEEN_RETRIES = 1000;

    public static VistaLinkConnectionX create(String sta3n, String duz)
            throws VistaLinkConnectionException {

        return VistaLinkConnectionX.builder()
                .sta3n(sta3n)
                .conn(VistaLinkUtils.getConnectionFromUserInfo(sta3n, null, duz,
                        CONNECTION_ATTEMPTS, WAIT_BETWEEN_RETRIES))
                .build();

    }

    /**
     * return a VistaApiVlc containing a vista link connection
     * 
     * validate that the user has access to the station specified in the token
     * 
     * @stationNo                                may be an sta6a; the jwt token only has sta3n
     *                                           designations. so we are only checking for access to
     *                                           the root station.
     * 
     *                                           This assumes that all sta6a divisions begin with
     *                                           the first 3 digits of the root stationNo
     * 
     * 
     * 
     * @param     stationNo
     * @param     duz
     * @param     user
     * @return
     * @throws    VistaLinkConnectionException
     * @throws    InvalidStationMappingException
     */
    public static VistaLinkConnectionX create(String stationNo, String duz, JwtUserPrincipal user)
            throws VistaLinkConnectionException, InvalidStationMappingException {

        VistaId vistaId = null;

        if (user.isSsoiToken()) {
            vistaId = user.getVista(stationNo);
        } else {
            vistaId = user.getVista(stationNo, duz);
        }

        if (vistaId == null) {
            throw new InvalidStationMappingException(invalidStationErr(stationNo, user));
        }

        return VistaLinkConnectionX.builder()
                .sta3n(stationNo)
                .conn(VistaLinkUtils.getConnectionFromUserInfo(stationNo, null, duz,
                        CONNECTION_ATTEMPTS, WAIT_BETWEEN_RETRIES))
                .build();

    }

    /**
     * only valid for non-ssoi token; do not use for ssoi JWT
     * 
     * @param  sta3n
     * @param  user
     * @return
     * @throws VistaLinkConnectionException
     */
    @Deprecated
    public static VistaLinkConnectionX create(String sta3n, JwtUserPrincipal user)
            throws VistaLinkConnectionException, InvalidStationMappingException {

        if (user.isSsoiToken()) {
            log.error("invalid token type");
        }

        VistaId vistaId = null;
        vistaId = user.getVista(sta3n);

        if (vistaId == null) {
            throw new InvalidStationMappingException(invalidStationErr(sta3n, user));
        }

        return VistaLinkConnectionX.builder()
                .sta3n(sta3n)
                .conn(VistaLinkUtils.getConnectionFromUserInfo(sta3n, null, vistaId.getDuz(),
                        CONNECTION_ATTEMPTS, WAIT_BETWEEN_RETRIES))
                .build();

    }

    private static String invalidStationErr(String sta3n, JwtUserPrincipal user) {

        String sta3ns = user.getVistaIds()
                .stream()
                .map(VistaId::getSiteId)
                .collect(Collectors.joining("^"));
        return MAPPING_ERR + ":  request for: " + sta3n + " -- token valid for: [" + sta3ns + "]";

    }

}
