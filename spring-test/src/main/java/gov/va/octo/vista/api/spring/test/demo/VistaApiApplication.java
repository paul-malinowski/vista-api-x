package gov.va.octo.vista.api.spring.test.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import gov.va.octo.vista.api.spring.test.demo.rest.Appts;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class VistaApiApplication implements CommandLineRunner {

    @Autowired
    private Appts appts;
    
	public static void main(String[] args) {
		SpringApplication.run(VistaApiApplication.class, args);
	}

	
	@Override
	public void run(String... args) throws Exception {
	    
	    appts.getApptByClinic();
	    
	   
	}
	
}
