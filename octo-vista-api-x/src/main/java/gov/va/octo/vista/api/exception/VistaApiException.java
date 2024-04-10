package gov.va.octo.vista.api.exception;

/**
 * Thrown if errors occur trying to map user to vista
 *
 * @author william.mccarty@va.gov
 */
public class VistaApiException extends BaseJsonException {

    private static final long serialVersionUID = 3766192210633206272L;

    public static final String ERROR_CODE = "VISTA-API-EXCEPTION-781654";

    public VistaApiException(String message) {
        super(ERROR_CODE, message);
    }

}
