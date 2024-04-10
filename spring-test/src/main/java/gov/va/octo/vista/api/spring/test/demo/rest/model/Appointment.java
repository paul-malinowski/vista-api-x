package gov.va.octo.vista.api.spring.test.demo.rest.model;

import java.util.List;

import lombok.Data;

@Data
public class Appointment {

    private String appointmentCancelled;
    private String appointmentDateTime;
    private String appointmentIEN;
    private String appointmentType;
    private String appointmentTypeIEN;
    private String appointmentTypeSubCategory;
    private String autoRebookedApptDateTime;
    private String cancellationReason;
    private String cancellationRemarks;
    private String checkIn;
    private String checkInEntered;
    
    private List<CheckInStep> checkInSteps;
    
    private String CheckInUser;
    private String CheckOut;
    private String CheckOutEntered;
    private String CheckOutUser;
    private String Clinic;
    private String CollateralVisit;
    private String ConsultLink;
    private String CurrentStatus;
    private String DataEntryClerk;
    private String DateAppointmentMade;
    private String DesiredDateOfAppointment;
    private String DuplicateApptSameDay;
    private String EASTrackingNumber;
    private String EkgDateTime;
    private String EncounterConversionStatus;
    private String EncounterFormsAsAddOns;
    private String EncounterFormsPrinted;
    private String EndTime;
    private String FollowUpVisit;
    private String LabDateTime;
    private String LengthOfAppt;
    private String NextAvaApptIndicator;
    private String NoShowCancelDateTime;
    private String NoShowCancelledBy;
    
    private List<String> note;
    
    private String NumberOfCollateralSeen;
    private String OtherTests;
    private String OtherTravel;
    private String OutpatientEncounter;
    private String OverLaidAppointmentData;
    private String OverbookFlag;
    private String ParentRecordRequest;
    
    private Patient patient;
    
    private String PriorXRayResults;

    private Provider provider;
    
    private String PurposeOfVisit;
    private String RealAppointment;

    private Resource resource;

    private String ResourceIEN;
    private String RoutingSlipPrintDate;
    private String RoutingSlipPrinted;
    private String SchedulerName;
    private String SchedulingApplication;
    private String SchedulingRequestType;
    private String SpecialSurveyDisposition;
    private String StartTimeFM;
    private String Status;
    private String VeteranVideoCallURL;
    private String WardLocation;
    private String XRAY;
    private String XrayDateTime;
    
}
