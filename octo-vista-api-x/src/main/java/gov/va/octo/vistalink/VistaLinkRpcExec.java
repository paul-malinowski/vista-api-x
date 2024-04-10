package gov.va.octo.vistalink;

import static gov.va.octo.vista.api.utils.LogSanitizer.sanitize;
import java.io.IOException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.va.med.exception.FoundationsException;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnection;
import gov.va.med.vistalink.adapter.record.VistaLinkFaultException;
import gov.va.med.vistalink.rpc.RpcRequest;
import gov.va.med.vistalink.rpc.RpcRequestFactory;
import gov.va.med.vistalink.rpc.RpcResponse;
import gov.va.octo.vista.api.client.RpcRequestX;
import gov.va.octo.vista.api.client.RpcResponseX;
import gov.va.octo.vista.api.client.VistaConstants;
import gov.va.octo.vistalink.model.RpcLogMessage;
import gov.va.octo.vistalink.model.VistaLinkConnectionX;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class VistaLinkRpcExec {


    /**
     * 
     * make an RPC call
     * 
     * @param  conn
     * @param  req
     * @return
     * @throws FoundationsException
     */
    public static RpcResponseX<String> execute(VistaLinkConnectionX vlc, RpcRequestX xreq)
            throws FoundationsException {

        if (log.isInfoEnabled()) {
            log.info(buildRpcLogMsg(vlc, xreq));
        }

        RpcRequest req = buildRequest(xreq);

        RpcResponseX<String> result = new RpcResponseX<>();

        String strResponse = executeRpc(vlc.getConn(), req, xreq.getTimeout());
        log.debug(xreq.getRpc() + ":  " + strResponse);

        result.setPayload(strResponse);
        return result;

    }


    /**
     * 
     * make an RPC call
     * 
     * @param  conn
     * @param  req
     * @return
     * @throws FoundationsException
     */
    public static RpcResponseX<JsonNode> executeJson(VistaLinkConnectionX vlc, RpcRequestX xreq)
            throws FoundationsException {

        RpcResponseX<JsonNode> result = new RpcResponseX<>();

        RpcResponseX<String> vr = execute(vlc, xreq);

        String strResponse = vr.getPayload();

        try {

            // remove char(13) & char(10)
            strResponse = strResponse.replace("\n", "").replace("\r", "");
            ObjectMapper mapper = new ObjectMapper();

            JsonNode root = mapper.readTree(strResponse);
            result.setPayload(root);

            return result;

        } catch (IOException e) {
            log.error("io exception", e);
            throw new FoundationsException(e);
        }
    }


    /**
     * create an RpcRequest
     * 
     * @param  conn
     * @param  context
     * @param  rpc
     * @param  params
     * @return
     * @throws FoundationsException
     */
    public static RpcRequest buildRequest(RpcRequestX xreq) throws FoundationsException {


        RpcRequest req = RpcRequestFactory.getRpcRequest();

        req.setRpcContext(xreq.getContext());
        req.setRpcName(xreq.getRpc());

        if (xreq.version().isPresent()) {
            req.setRpcVersion(xreq.version().get());
        }

        for (int i = 0; i < xreq.getParameters().size(); i++) {
            var parameter = xreq.getParameters().get(i);
            log.debug("adding parameter: " + parameter.type() + " : " + parameter.value());
            req.getParams().setParam(i + 1, parameter.type(), parameter.value());
        }

        return req;

    }


    private static String buildRpcLogMsg(VistaLinkConnectionX vlc, RpcRequestX xreq) {

        RpcLogMessage rpcl = RpcLogMessage.builder().station(vlc.getSta3n()).xreq(xreq).build();

        return "[RPC] " + sanitize(rpcl.toString());

    }

    /**
     * 
     * @param  conn
     * @param  req
     * @return
     * @throws VistaLinkFaultException
     * @throws FoundationsException
     */
    public static String executeRpc(VistaLinkConnection conn, RpcRequest req, int timeout)
            throws VistaLinkFaultException, FoundationsException {

        if (timeout < VistaConstants.DEFAULT_REQUEST_TIMEOUT) {
            timeout = VistaConstants.DEFAULT_REQUEST_TIMEOUT;
        }
        conn.setTimeOut(timeout);
        RpcResponse response = conn.executeRPC(req);
        // reset the timeout
        conn.setTimeOut(VistaConstants.DEFAULT_REQUEST_TIMEOUT);
        return response.getResults();

    }


}
