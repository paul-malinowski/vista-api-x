package gov.va.octo.vista.api.client;

import java.io.Serializable;

import lombok.Data;

@Data
public class RpcResponseX<T> implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String path;
	private T payload;
	
}
