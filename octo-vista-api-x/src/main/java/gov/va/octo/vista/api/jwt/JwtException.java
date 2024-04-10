package gov.va.octo.vista.api.jwt;

import javax.ws.rs.core.Response.Status;

/**
 * Thrown if errors occur during the authorization process.
 *
 * @author william.mccarty@va.gov
 */
public class JwtException {

    public static class AccessDeniedException extends AbstractJwtException {

        private static final long serialVersionUID = 140850945492330633L;

        public static final String errorCode = "JWT-ACCESS_DENIED-0001";
        public static final Status status = Status.FORBIDDEN;

        public AccessDeniedException(String message) {
            super(errorCode, status, message);
        }

        public AccessDeniedException(String message, Throwable cause) {
            super(errorCode, status, message, cause);
        }

    }

    public static class AuthenticationException extends AbstractJwtException {

        private static final long serialVersionUID = 5927119873728346827L;

        public static final String errorCode = "JWT-AUTHENTICATION-ERROR-0002";
        public static final Status status = Status.FORBIDDEN;

        public AuthenticationException(String message) {
            super(errorCode, status, message);
        }

        public AuthenticationException(String message, Throwable cause) {
            super(errorCode, status, message, cause);
        }
    }

    public static class InvalidTokenException extends AbstractJwtException {

        private static final long serialVersionUID = -6958898202088623688L;

        public static final String errorCode = "JWT-INVALID-TOKEN-0003";
        public static final Status status = Status.BAD_REQUEST;

        public InvalidTokenException(String message) {
            super(errorCode, status, message);
        }

        public InvalidTokenException(String message, Throwable cause) {
            super(errorCode, status, message, cause);
        }
    }

    public static class TokenRefreshException extends AbstractJwtException {

        private static final long serialVersionUID = -2808175940196884023L;

        public static final String errorCode = "JWT-TOKEN-REFRESH-ERROR-0004";
        public static final Status status = Status.BAD_REQUEST;

        public TokenRefreshException(String message) {
            super(errorCode, status, message);
        }

        public TokenRefreshException(String message, Throwable cause) {
            super(errorCode, status, message, cause);
        }
    }

}
