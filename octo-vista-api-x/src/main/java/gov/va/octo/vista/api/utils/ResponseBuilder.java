package gov.va.octo.vista.api.utils;

import javax.ws.rs.core.UriInfo;
import gov.va.octo.vista.api.client.VistaApiResponse;

public class ResponseBuilder {

    /**
     * 
     * @param  <T>
     * @param  uriInfo
     * @return
     */
    public static <T> VistaApiResponse<T> build(T data, UriInfo uriInfo) {
        VistaApiResponse<T> vrr = new VistaApiResponse<T>();
        vrr.setPath(uriInfo.getAbsolutePath().getPath());
        vrr.setData(data);
        return vrr;
    }


    public static VistaApiResponse<Boolean> build(UriInfo uriInfo) {
        VistaApiResponse<Boolean> vrr = new VistaApiResponse<Boolean>();
        vrr.setPath(uriInfo.getAbsolutePath().getPath());
        vrr.setData(Boolean.TRUE);
        return vrr;
    }
}
