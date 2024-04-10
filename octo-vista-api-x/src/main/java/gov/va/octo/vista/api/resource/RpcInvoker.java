package gov.va.octo.vista.api.resource;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import gov.va.med.vistalink.adapter.record.VistaLinkFaultException;
import gov.va.octo.vista.api.client.RpcRequestX;
import gov.va.octo.vista.api.client.RpcResponseX;
import gov.va.octo.vista.api.exception.VistaApiException;
import gov.va.octo.vista.api.jwt.JwtException.AccessDeniedException;
import gov.va.octo.vista.api.jwt.JwtUserPrincipal;
import gov.va.octo.vista.api.utils.EndpointLogBuilder;
import gov.va.octo.vistalink.VistaLinkConnectionCreator;
import gov.va.octo.vistalink.VistaLinkRpcExec;
import gov.va.octo.vistalink.model.VistaLinkConnectionX;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.extern.slf4j.Slf4j;

@Path("vista-sites/{stationNo}/users/{duz}/rpc")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Slf4j
public class RpcInvoker extends BaseResource {

    private static String VERSION = "1.0";

    public String getVersion() {
        return VERSION;
    }

    private final static int RETRY_COUNT = 3;

    @POST
    @Operation(
            summary = "execute RPC",
            description = "return data for given RPC; !!only allowed in non-production environment")
    @Path("/invoke")
    @SuppressWarnings("rawtypes")
    public RpcResponseX invoke(
            @Valid RpcRequestX xreq,
            @Parameter(
                    name = "stationNo",
                    in = ParameterIn.PATH,
                    description = "stationNo of VistA system; should match stationNo in bearer token",
                    example = "600",
                    required = true)
            @PathParam("stationNo") String stationNo,
            @Parameter(
                    name = "duz",
                    in = ParameterIn.PATH,
                    description = "identifier of vista user performing action",
                    example = "12341234",
                    required = true)
            @PathParam("duz") String duz) throws Exception {


        JwtUserPrincipal user = this.getCurrentUser();

        if (log.isInfoEnabled()) {
            log.info(buildLogMsg(stationNo, duz, xreq));
        }

        if (!user.allowConnection(stationNo, duz) || !user.allowExecution(xreq)) {
            if (log.isWarnEnabled()) {
                log.warn(EndpointLogBuilder.buildAuthorizationFailure(servletRequest, VERSION,
                        user.getApplication(), stationNo, duz, xreq));
            }
            throw new AccessDeniedException("You don't have permissions to perform this action.");
        }


        VistaLinkConnectionX vlc = null;

        try {

            vlc = VistaLinkConnectionCreator.create(stationNo, duz, RETRY_COUNT, xreq.getTimeout());

            if (xreq.isJsonResult()) {
                return fillOut(VistaLinkRpcExec.executeJson(vlc, xreq));
            } else {
                return fillOut(VistaLinkRpcExec.execute(vlc, xreq));
            }

        } catch (VistaLinkFaultException | VistaApiException e) {
            if (log.isWarnEnabled()) {
                log.warn(buildLogError(stationNo, duz, e));
            }
            throw e;
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error(buildLogError(stationNo, duz, e), e);
            }
            throw e;
        } finally {
            try {
                if (vlc != null)
                    vlc.getConn().close();
            } catch (Exception e) {
                /* no op */
            }
        }



    }



}
