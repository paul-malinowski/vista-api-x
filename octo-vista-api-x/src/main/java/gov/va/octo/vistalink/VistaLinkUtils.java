package gov.va.octo.vistalink;

import gov.va.med.exception.FoundationsException;
import gov.va.med.vistalink.adapter.cci.VistaLinkAppProxyConnectionSpec;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnection;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnectionFactory;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnectionSpec;
import gov.va.med.vistalink.adapter.cci.VistaLinkDuzConnectionSpec;
import gov.va.med.vistalink.adapter.cci.VistaLinkVpidConnectionSpec;
import gov.va.med.vistalink.institution.InstitutionMappingDelegate;
import gov.va.med.vistalink.institution.InstitutionMappingNotFoundException;
import gov.va.med.vistalink.rpc.RpcRequest;
import gov.va.med.vistalink.rpc.RpcResponse;
import gov.va.octo.vistalink.exception.VistaLinkConnectionException;
import gov.va.octo.vistalink.exception.VistaLinkRpcException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.resource.ResourceException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VistaLinkUtils {

  private static final Logger log = LoggerFactory.getLogger(VistaLinkUtils.class);
  public static final char UP = '^';

  public static final String VISTALINK_CONNECTION_ERROR = "vistalink.connection.error";
  public static final String VISTALINK_RPC_ERROR = "vistalink.rpc.error";
  public static final String VISTALINK_FOUNDATIONS_ERROR = "vistalink.foundations.error";

  private static final String ERROR_CODE_UNABLE_TO_GET_CONNECTION = "AD7J-V6XL-N84C-AFMH";
  private static final String ERROR_CODE_INSTITUTION_MAP_ERROR = "6BKQ-73RR-B2BO-ODAV";
  private static final String ERROR_CODE_TOO_MANY_CONNECTION_ATTEMPTS = "IBYP-HECB-UK60-6D5Y";
  private static final String ERROR_CODE_JNDI_NAMING_EXCEPTION = "9MQ6-DRKT-X0PH-XUV0";

  /**
   * Obtain a VistaLinkConnection from the passed in user information.
   *
   * <p>will retry the connection attempt @maxAttempts will @sleep for X milliseconds between
   * attempts
   *
   * <p>IMPORTANT!!!!! The caller is responsible for closing the connection when RPC complete
   *
   * @param stationNo
   * @param userVpid
   * @param userDuz
   * @return
   * @throws VistaLinkConnectionException
   */
  public static VistaLinkConnection getConnectionFromUserInfo(
      String stationNo, String userVpid, String userDuz, int maxAttempts, int sleep)
      throws VistaLinkConnectionException {

    if (maxAttempts < 1) maxAttempts = 3; // default to 3 if < 1
    if (maxAttempts > 50) maxAttempts = 50; // max attempts 50
    if (sleep < 250) sleep = 250; // min 1/4 sec
    if (sleep > 30000) sleep = 30 * 1000; // max 30 secs

    int attempt = 1;
    while (true) {
      try {

        if (attempt > 1 && log.isInfoEnabled()) {
          log.info("connection retry { stationNo: " + stationNo + ", attempt: " + attempt + " }");
        }
        return getConnectionFromUserInfo(stationNo, userVpid, userDuz);

      } catch (Exception e) {

        if (e instanceof VistaLinkConnectionException) {
          if (((VistaLinkConnectionException) e)
              .getErrorCode()
              .equals(ERROR_CODE_INSTITUTION_MAP_ERROR)) {
            // just abort; can't recover
            throw e;
          }
        }

        if (attempt++ > maxAttempts) {
          throw new VistaLinkConnectionException(
              ERROR_CODE_UNABLE_TO_GET_CONNECTION,
              "unable to get connection",
              "Server",
              "Server",
              "Internal Application Error",
              e.getMessage());
        }
        try {
          Thread.sleep(sleep);
        } catch (InterruptedException ex) {
          Thread.currentThread().interrupt(); // very important
          break;
        }
      }
    }

    // shouldn't ever get to there
    throw new VistaLinkConnectionException(
        ERROR_CODE_TOO_MANY_CONNECTION_ATTEMPTS,
        "unknown error condition",
        "Server",
        "Server",
        "Internal Application Error",
        "unknown error: too many connection attempts");
  }

  /**
   * Obtain a VistaLinkConnection from the passed in user information.
   *
   * <p>IMPORTANT!!!!! The caller is responsible for closing the connection when RPC complete
   *
   * @param stationNo
   * @param userVpid
   * @param userDuz
   * @return
   * @throws VistaLinkConnectionException
   */
  public static VistaLinkConnection getConnectionFromUserInfo(
      String stationNo, String userVpid, String userDuz) throws VistaLinkConnectionException {

    try {
      // Create a connection if necessary
      VistaLinkConnectionSpec connSpec = null;
      Context ic = new InitialContext();
      if (!StringUtils.isBlank(userVpid)) {
        connSpec = new VistaLinkVpidConnectionSpec(stationNo, userVpid);
      } else {
        connSpec = new VistaLinkDuzConnectionSpec(stationNo, userDuz);
      }

      return getConnectionForSpec(connSpec, ic);

    } catch (NamingException e) {
      log.error("context exception", e);
      throw new VistaLinkConnectionException(
          ERROR_CODE_JNDI_NAMING_EXCEPTION,
          "jndi naming exception",
          "Server",
          "Server",
          "Internal Application Error",
          e.getMessage());
    }
  }

  /**
   * Obtain a VistaLinkConnection from the passed in user information.
   *
   * <p>IMPORTANT!!!!! The caller is responsible for closing the connection when RPC complete
   *
   * @param stationNo
   * @param userDuz
   * @return
   * @throws VistaLinkConnectionException
   */
  public static VistaLinkConnection getConnectionFromUserInfoDuz(String stationNo, String userDuz)
      throws VistaLinkConnectionException {

    return getConnectionFromUserInfo(stationNo, "", userDuz);
  }

  /**
   * Obtain a VistaLinkConnection from the passed in user information.
   *
   * <p>IMPORTANT!!!!! The caller is responsible for closing the connection when RPC complete
   *
   * @param stationNo
   * @param appProxy
   * @return
   * @throws VistaLinkConnectionException
   */
  public static VistaLinkConnection getConnectionFromAppProxy(String stationNo, String appProxy)
      throws VistaLinkConnectionException {

    if (StringUtils.isBlank(stationNo)) {
      log.error("must pass in a valid stationNo");
      throw new VistaLinkConnectionException("must pass in a valid stationNo");
    }

    if (StringUtils.isBlank(appProxy)) {
      log.error("must pass in a valid appProxy");
      throw new VistaLinkConnectionException("must pass in a valid appProxy");
    }

    try {
      // Create a connection if necessary
      VistaLinkConnectionSpec connSpec = null;
      Context ic = new InitialContext();
      connSpec = new VistaLinkAppProxyConnectionSpec(stationNo, appProxy);

      return getConnectionForSpec(connSpec, ic);

    } catch (Exception e) {
      log.error("Error obtaining VistaLink connection", e);
      throw new VistaLinkConnectionException(
          ERROR_CODE_UNABLE_TO_GET_CONNECTION,
          "unable to get connection",
          "Server",
          "Server",
          "Internal Application Error",
          e.getMessage());
    }
  }

  private static VistaLinkConnection getConnectionForSpec(
      VistaLinkConnectionSpec connSpec, Context context) throws VistaLinkConnectionException {

    VistaLinkConnection conn = null;

    try {
      String jndiName =
          InstitutionMappingDelegate.getJndiConnectorNameForInstitution(connSpec.getDivision());
      VistaLinkConnectionFactory cf = (VistaLinkConnectionFactory) context.lookup(jndiName);
      conn = (VistaLinkConnection) cf.getConnection(connSpec);

      conn.setTimeOut(15000);

    } catch (InstitutionMappingNotFoundException e) {
      log.error("Error obtaining VistaLink connection: " + e.getMessage());
      throw new VistaLinkConnectionException(
          ERROR_CODE_INSTITUTION_MAP_ERROR,
          "institution-map-error",
          "Server",
          "Server",
          "Internal Application Error",
          e.getMessage());
    } catch (FoundationsException e) {
      // swallow stack trace assuming it has been spit out in the VL library
      log.error("Error obtaining VistaLink connection: " + e.getMessage());
      throw new VistaLinkConnectionException(
          ERROR_CODE_UNABLE_TO_GET_CONNECTION,
          "unable to get connection",
          "Server",
          "Server",
          "Internal Application Error",
          e.getMessage());
    } catch (Exception e) {
      log.error("Error obtaining VistaLink connection", e);
      throw new VistaLinkConnectionException(
          ERROR_CODE_UNABLE_TO_GET_CONNECTION,
          "unable to get connection",
          "Server",
          "Server",
          "Internal Application Error",
          e.getMessage());
    }

    return conn;
  }

  /**
   * Call an rpc from the RpcRequest passed in. Makes sure that the connection is closed after the
   * RPC has been called.
   *
   * @param req
   * @param stationNo
   * @param userVpid
   * @param userDuz
   * @return
   * @throws VistaLinkRpcException
   * @throws VistaLinkConnectionException
   */
  public static RpcResponse call(RpcRequest req, String stationNo, String userVpid, String userDuz)
      throws VistaLinkRpcException, VistaLinkConnectionException {

    RpcResponse result = null;
    VistaLinkConnection conn = null;

    try {

      conn = getConnectionFromUserInfo(stationNo, userVpid, userDuz);

      req.setTimeOut(conn.getTimeOut() * 2);
      result = conn.executeRPC(req);

    } catch (FoundationsException e) {
      log.error("RPC Error", e);
      throw new VistaLinkRpcException(e);
    } finally {
      try {
        if (conn != null) {
          conn.close();
        }
      } catch (ResourceException re) {
        log.error("Resource Exception", re);
      }
    }
    // log.debug("Function call: finished successfully.");
    return result;
  }
}
