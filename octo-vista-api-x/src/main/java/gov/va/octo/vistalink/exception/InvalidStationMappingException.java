package gov.va.octo.vistalink.exception;

import gov.va.med.vistalink.adapter.record.VistaLinkFaultException;
import gov.va.med.vistalink.security.m.SecurityFaultException;

/**
 * Thrown if errors occur trying to map user to vista
 *
 * @author amccarty
 */
public class InvalidStationMappingException extends SecurityFaultException {

	private static final long serialVersionUID = -5571971352136195777L;
	
	public static final String ERROR_CODE = "L9TWB-YIUHG-NT9BR-EQO48";

	public InvalidStationMappingException(String errmsg) {
		super(new VistaLinkFaultException(ERROR_CODE, 
				errmsg, 
				"VISTA-STATION-MAPPING", "VISTA-STATION-MAPPING", 
				"Server", "Internal Application Error"));
	}
	
	
}
