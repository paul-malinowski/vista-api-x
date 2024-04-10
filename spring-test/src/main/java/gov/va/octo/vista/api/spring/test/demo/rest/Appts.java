package gov.va.octo.vista.api.spring.test.demo.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import gov.va.octo.vista.api.client.AuthenticationToken;
import gov.va.octo.vista.api.client.RpcRequestX;
import gov.va.octo.vista.api.client.RpcRequestX.Parameter;
import gov.va.octo.vista.api.spring.test.demo.rest.model.Appointment;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class Appts {

    @Autowired
    private VistaApiConfig config;
    
    @Autowired
    private TokenRequest tokenRequest;
    
    
    
    
    public void getApptByClinic() {
        
        RestTemplate restTemplate = new RestTemplate();
        
        AuthenticationToken token = tokenRequest.fetch();
        
        RpcRequestX req = RpcRequestX.builder()
                .context("SDESRPC")
                .rpc("SDES GET APPTS BY CLIN IEN 2")
                .jsonResult(true)
                .parameters(List.of(
                        Parameter.builder().string("195").build(),
                        Parameter.builder().string("2022-06-10T00:00-05:00").build(),
                        Parameter.builder().string("2022-06-20T00:00-05:00").build()))
                .build();
        
        
//        String apptResponse = restTemplate.postForObject(
//                "http://localhost:8080/vista-api-x/api/rpc/vista-sites/500/users/520824652/invoke",
//                req,
//                String.class);
        
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization" , "Bearer " + token.getToken());
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<String> entity = new HttpEntity(req, headers);

        ResponseEntity<JsonNode> response = null;
        
        UriComponents rpcInvokeUrl = UriComponentsBuilder
                .fromHttpUrl(config.getRpcInvokeUrl())
                .buildAndExpand(config.getStationNo(), config.getDuz());
        
        try {
            response = restTemplate.exchange(rpcInvokeUrl.toUriString(),
                    HttpMethod.POST,
                    entity, 
                    JsonNode.class);
        } catch (Exception e) {
            log.error("error occurred");
            e.printStackTrace();
        }
        
        //log.info(response.getBody().toPrettyString());
        

        ObjectMapper mapper = JsonMapper
                .builder()
                // required to ignore the case of the incoming json data from vista
                .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
                // required to accept "" and [""] as empty arrays that the vista serializer is sending us
                .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
                .build();
        
        JsonNode apptRoot = response.getBody().at("/payload/Appointment");
        
        List<Appointment> appointments = mapper.convertValue(apptRoot, new TypeReference<List<Appointment>>() {});
        
        for(Appointment appt : appointments) {
            log.info("appt ien: " + appt.getAppointmentIEN());
        }
    }
    
    
    
    
}
