package gov.va.octo.vista.api.exception;

public class BaseJsonException extends RuntimeException {


    private static final long serialVersionUID = 803652811677689337L;

    private String errorCode;


    public BaseJsonException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public BaseJsonException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }


}
