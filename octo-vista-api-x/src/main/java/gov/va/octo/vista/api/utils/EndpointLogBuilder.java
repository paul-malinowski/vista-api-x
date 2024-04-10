package gov.va.octo.vista.api.utils;

import static gov.va.octo.vista.api.utils.LogSanitizer.sanitize;
import javax.servlet.http.HttpServletRequest;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.va.med.vistalink.adapter.record.VistaLinkFaultException;
import gov.va.octo.vista.api.client.RpcRequestX;
import lombok.Builder;
import lombok.Getter;

public class EndpointLogBuilder {

    public static final String ENDPOINT_PREFIX = "[ENDPOINT]";
    public static final String ERROR_PREFIX = "[ERROR]";
    public static final String VISTA_FAULT_PREFIX = "[VISTA-FAULT]";
    public static final String AUTHORIZATION_PREFIX = "[AUTHORIZATION-FAILURE]";

    @Builder
    @Getter
    @JsonInclude(Include.NON_NULL)
    public static class EndpointTemplate {
        String endpoint;
        @JsonProperty("http-method")
        String httpMethod;
        String version;
        String station;
        String duz;
        RpcRequestX request;
    }

    @Builder
    @Getter
    @JsonInclude(Include.NON_NULL)
    public static class AuthorizationFailureTemplate {
        String endpoint;
        @JsonProperty("http-method")
        String httpMethod;
        String version;
        String app;
        String stationNo;
        String duz;
        RpcRequestX request;
    }

    @Builder
    @Getter
    @JsonInclude(Include.NON_NULL)
    public static class ErrorTemplate {
        String endpoint;
        String station;
        String duz;
        @JsonProperty("fault-code")
        String faultCode;
        @JsonProperty("fault-string")
        String faultString;
        @JsonProperty("fault-actor")
        String faultActor;
        @JsonProperty("error-code")
        String errorCode;
        @JsonProperty("error-type")
        String errorType;
        String message;
    }

    public static String build(
            HttpServletRequest hsr,
            String version,
            String station,
            String duz,
            RpcRequestX xreq) {

        EndpointTemplate tpl = EndpointTemplate.builder()
                .endpoint(hsr.getPathInfo())
                .httpMethod(hsr.getMethod())
                .version(version)
                .station(station)
                .duz(duz)
                .request(xreq)
                .build();

        return build(tpl, ENDPOINT_PREFIX);

    }

    public static String buildError(
            HttpServletRequest hsr,
            String station,
            String duz,
            Exception e) {

        ErrorTemplate tpl = null;

        if (e instanceof VistaLinkFaultException) {

            VistaLinkFaultException vf = (VistaLinkFaultException) e;

            tpl = ErrorTemplate.builder()
                    .endpoint(sanitize(hsr.getPathInfo()))
                    .station(sanitize(station))
                    .duz(sanitize(duz))
                    .message(e.getMessage())
                    .faultCode(vf.getFaultCode())
                    .faultString(vf.getFaultString())
                    .faultActor(vf.getFaultActor())
                    .errorCode(vf.getErrorCode())
                    .errorType(vf.getErrorType())
                    .build();

            return build(tpl, VISTA_FAULT_PREFIX);

        } else {

            tpl = ErrorTemplate.builder()
                    .endpoint(sanitize(hsr.getPathInfo()))
                    .station(sanitize(station))
                    .duz(sanitize(duz))
                    .message(e.getMessage())
                    .build();

            return build(tpl, ERROR_PREFIX);
        }
    }

    public static String build(Object data, String prefix) {

        String msg;
        ObjectMapper mapper = new ObjectMapper();
        try {
            msg = prefix + " " + mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            msg = prefix + " " + data.toString();
        }

        return sanitize(msg);

    }


    public static String buildAuthorizationFailure(
            HttpServletRequest hsr,
            String version,
            String app,
            String stationNo,
            String duz,
            RpcRequestX xreq) {



        AuthorizationFailureTemplate tpl = AuthorizationFailureTemplate.builder()
                .endpoint(sanitize(hsr.getPathInfo()))
                .httpMethod(sanitize(hsr.getMethod()))
                .version(sanitize(version))
                .app(sanitize(app))
                .stationNo(sanitize(stationNo))
                .duz(sanitize(duz))
                .request(xreq)
                .build();

        return build(tpl, AUTHORIZATION_PREFIX);

    }


}
