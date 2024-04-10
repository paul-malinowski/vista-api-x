package gov.va.octo.vista.api.exception.mapper;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import gov.va.med.vistalink.adapter.record.VistaLinkFaultException;

/**
 * Exception mapper for {@link VistaLinkFaultExceptionMapper}s.
 *
 * @author william.mccarty@va.gov
 */
@Provider
public class VistaLinkFaultExceptionMapper implements ExceptionMapper<VistaLinkFaultException> {

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(VistaLinkFaultException vfe) {

        Status status = Status.BAD_REQUEST;

        ApiErrorResponseDetails errorDetails = new ApiErrorResponseDetails();
        errorDetails.setSuccess(false);
        errorDetails.setErrorCode(vfe.getErrorCode());
        errorDetails.setResponseStatus(status.getStatusCode());
        errorDetails.setTitle(status.getReasonPhrase());
        errorDetails.setMessage(vfe.getErrorMessage());
        errorDetails.setErrorType(vfe.getErrorType());
        errorDetails.setFaultActor(vfe.getFaultActor());
        errorDetails.setFaultCode(vfe.getFaultCode());
        errorDetails.setFaultString(vfe.getFaultString());
        errorDetails.setPath(uriInfo.getAbsolutePath().getPath());

        return Response.status(status)
                .entity(errorDetails)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
