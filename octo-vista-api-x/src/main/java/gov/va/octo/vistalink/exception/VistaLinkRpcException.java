package gov.va.octo.vistalink.exception;

import gov.va.med.vistalink.adapter.record.VistaLinkFaultException;

public class VistaLinkRpcException extends VistaLinkFaultException {

	private static final long serialVersionUID = 2117744533415749204L;

	public VistaLinkRpcException(){
        super();
    }
    
    public VistaLinkRpcException(Exception e){
        super(e);
    }
    
    public VistaLinkRpcException(String msg, Exception e){
        super(msg,e);
    }
    
    public VistaLinkRpcException(String msg){
        super(msg);
    }
    
}
