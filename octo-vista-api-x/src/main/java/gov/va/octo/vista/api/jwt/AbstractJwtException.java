package gov.va.octo.vista.api.jwt;

import javax.ws.rs.core.Response.Status;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public abstract class AbstractJwtException extends RuntimeException {

    private static final long serialVersionUID = 803652811677689337L;

    private String errorCode;
    private Status status;

    public AbstractJwtException(String errorCode, Status status, String message) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }

    public AbstractJwtException(String errorCode, Status status, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.status = status;
    }

}
