package gov.va.octo.vista.api.spring.test.demo.rest.model;

import lombok.Data;

@Data
public class Patient {

    private String CurrentEligibilityCode;
    private String DFN;
    private String EligibilityIEN;
    private String EnrollmentCode;
    private String EnrollmentDate;
    private String Last4;
    private String Name;
    private String SSN;
    private String Street;
    
}
