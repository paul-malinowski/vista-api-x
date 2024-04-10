package gov.va.octo.vistalink.exception;

import gov.va.med.vistalink.adapter.record.VistaLinkFaultException;

public class VistaLinkConnectionException extends VistaLinkFaultException {

	private static final long serialVersionUID = -8755480229223064728L;

	public VistaLinkConnectionException(){
        super();
    }
    
    public VistaLinkConnectionException(Exception e){
        super(e);
    }
    
    public VistaLinkConnectionException(String errorCode, String errorMessage, String errorType, String faultActor,
			String faultCode, String faultString) {
		super(errorCode, errorMessage, errorType, faultActor, faultCode, faultString);
	}
    
    public VistaLinkConnectionException(String msg, Exception e){
        super(msg,e);
    }
    
    public VistaLinkConnectionException(String msg){
        super(msg);
    }
    
}
