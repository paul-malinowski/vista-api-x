package gov.va.octo.vistalink.model;

import gov.va.med.vistalink.adapter.cci.VistaLinkConnection;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VistaLinkConnectionX {

	private VistaLinkConnection conn;
	private String sta3n;
	
	
	public boolean equals(Object b) {
		if(!(b instanceof VistaLinkConnectionX)) {
			return false;
		}
		return sta3n.equals(((VistaLinkConnectionX)b).sta3n);
	}
	
	public int hashCode() {
		return sta3n.hashCode();
	}
}
