package gov.va.octo.vista.api.spring.test.demo.rest;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "vista-api")
@Data
public class VistaApiConfig {

    private String tokenUrl;
    private String rpcInvokeUrl;
    private String key;
    private String stationNo;
    private String duz;
    
}
