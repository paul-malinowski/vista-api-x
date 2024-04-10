package gov.va.octo.vista.api.exception.mapper;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import gov.va.med.exception.FoundationsException;

/**
 * Exception mapper for {@link VistaApiExceptionMapper}s.
 *
 * @author william.mccarty@va.gov
 */
@Provider
public class FoundationsExceptionMapper implements ExceptionMapper<FoundationsException> {

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(FoundationsException exception) {

        Status status = Status.BAD_REQUEST;

        ApiErrorResponseDetails errorDetails = new ApiErrorResponseDetails();
        errorDetails.setSuccess(false);
        errorDetails.setErrorCode("FOUNDATION_EXCEPTION");
        errorDetails.setResponseStatus(status.getStatusCode());
        errorDetails.setTitle(status.getReasonPhrase());
        errorDetails.setMessage(exception.getMessage());
        errorDetails.setPath(uriInfo.getAbsolutePath().getPath());

        return Response.status(status)
                .entity(errorDetails)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
