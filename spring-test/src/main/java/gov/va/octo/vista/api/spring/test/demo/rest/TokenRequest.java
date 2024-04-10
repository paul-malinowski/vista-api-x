package gov.va.octo.vista.api.spring.test.demo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import gov.va.octo.vista.api.client.AuthenticationToken;
import gov.va.octo.vista.api.client.Credentials;
import gov.va.octo.vista.api.client.VistaApiResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TokenRequest {

    @Autowired
    private VistaApiConfig config;
    
    public AuthenticationToken fetch() {
        
        
        RestTemplate restTemplate = new RestTemplate();
        
        Credentials creds = new Credentials();
        creds.setKey(config.getKey());
        
        
        String tokenResponse = restTemplate.postForObject(
                "http://localhost:8080/vista-api-x/api/auth/token",
                creds,
                String.class);
        
        
        log.info(tokenResponse);
        
        ObjectMapper mapper = new ObjectMapper();
        JavaType jtype = mapper.getTypeFactory().constructParametricType(VistaApiResponse.class, AuthenticationToken.class);
        
        try {
            
            
            VistaApiResponse<AuthenticationToken> ar = mapper.readValue(tokenResponse, jtype);
            
            log.info(ar.getPath());
            log.info(ar.getData().getToken());
            
            return ar.getData();
            
            
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
        return null;
        
        
        
        
    }
    
    
    
}
