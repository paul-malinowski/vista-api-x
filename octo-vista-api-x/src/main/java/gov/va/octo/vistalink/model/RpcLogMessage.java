package gov.va.octo.vistalink.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import gov.va.octo.vista.api.client.RpcRequestX;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RpcLogMessage {

	String station;
	RpcRequestX xreq;
	
	@Override
	public String toString() {
		
		String msg;
		ObjectMapper mapper = new ObjectMapper();
		try {
			msg = mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			msg = this.getStation() + "^" + this.xreq.getContext()  + "^" + this.xreq.getRpc();
		}
		
		return msg;
	}
}
