package gov.va.octo.vista.api.resource;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import lombok.extern.slf4j.Slf4j;

@Path("hello")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Slf4j
public class HelloWorld extends BaseResource {

    private static String VERSION = "1.0";

    public String getVersion() {
        return VERSION;
    }

    @GET
    @PermitAll
    @Produces(MediaType.TEXT_PLAIN)
    @Path("world")
    public String helloWorld() {

        log.info("hello world");

        return "hello world";
    }



}
