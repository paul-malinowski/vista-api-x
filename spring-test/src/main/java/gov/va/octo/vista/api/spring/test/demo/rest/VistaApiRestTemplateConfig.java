package gov.va.octo.vista.api.spring.test.demo.rest;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

@Configuration
public class VistaApiRestTemplateConfig {

    
    @Bean(name = "vistaApiRestTemplate")
    public RestTemplate someRestTemplate(RestTemplateBuilder builder) {
        return builder.rootUri("http://localhost:8080/vista-api/api")
                .additionalInterceptors((ClientHttpRequestInterceptor) (request, body, execution) -> {
                    request.getHeaders().add("Bearer", "token");
                    return execution.execute(request, body);
                }).build();
    }
    
}
