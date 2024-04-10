package gov.va.octo.vista.api.exception.mapper;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ApiErrorResponseDetails {

    private boolean success;
    private String severity;
    private String errorCode;
    private int responseStatus;
    private String title;
    private String message;
    private String path;
    private String errorType;
    private String faultActor;
    private String faultCode;
    private String faultString;

}
