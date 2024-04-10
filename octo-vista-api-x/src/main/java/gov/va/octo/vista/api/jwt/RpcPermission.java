package gov.va.octo.vista.api.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RpcPermission {

    private String context;
    private String rpc;

}
