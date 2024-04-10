package gov.va.octo.vista.api.app;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Main class for Jersey
 * 
 * @author william.mccarty@va.gov
 *
 */

@ApplicationPath("api")
public class VistaApiXApp extends Application {

    public VistaApiXApp() {
        /* no op */
    }
}
