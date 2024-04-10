package gov.va.octo.vista.api.resource;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import gov.va.octo.vista.api.client.RpcRequestX;
import gov.va.octo.vista.api.client.RpcResponseX;
import gov.va.octo.vista.api.jwt.JwtUserPrincipal;
import gov.va.octo.vista.api.utils.EndpointLogBuilder;

public abstract class BaseResource {

    @Context
    protected SecurityContext securityContext;

    @Context
    protected UriInfo uriInfo;


    @Context
    protected HttpServletRequest servletRequest;


    public SecurityContext getSecurityContext() {
        return securityContext;
    }

    public UriInfo getUriInfo() {
        return uriInfo;
    }


    public JwtUserPrincipal getCurrentUser() {
        return (JwtUserPrincipal) this.securityContext.getUserPrincipal();
    }

    public abstract String getVersion();


    public String buildLogMsg(String station, String duz, RpcRequestX xreq) {

        return EndpointLogBuilder.build(servletRequest, getVersion(), station, duz, xreq);

    }

    public String buildLogError(String station, String duz, Exception e) {

        return EndpointLogBuilder.buildError(servletRequest, station, duz, e);

    }

    @SuppressWarnings("rawtypes")
    protected RpcResponseX fillOut(RpcResponseX response) {

        response.setPath(servletRequest.getPathInfo());
        return response;

    }


}
