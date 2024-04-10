package gov.va.octo.vista.api.jwt;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

/**
 * Exception mapper for {@link JwtException}s.
 *
 * @author william.mccarty@va.gov
 */
@Provider
public class JwtExceptionMapper implements ExceptionMapper<AbstractJwtException> {

    @Context
    private UriInfo uriInfo;


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Data
    public class ApiResponseDetails {
        private boolean success;
        private String severity;
        private String errorCode;
        private int responseStatus;
        private String title;
        private String message;
        private String path;
    }


    @Override
    public Response toResponse(AbstractJwtException exception) {

        ApiResponseDetails errorDetails = new ApiResponseDetails();
        errorDetails.setSuccess(false);
        errorDetails.setSeverity("Error");
        errorDetails.setErrorCode(exception.getErrorCode());
        errorDetails.setResponseStatus(exception.getStatus().getStatusCode());
        errorDetails.setTitle(exception.getStatus().getReasonPhrase());
        errorDetails.setMessage(exception.getMessage());
        errorDetails.setPath(uriInfo.getAbsolutePath().getPath());

        return Response.status(exception.getStatus())
                .entity(errorDetails)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
