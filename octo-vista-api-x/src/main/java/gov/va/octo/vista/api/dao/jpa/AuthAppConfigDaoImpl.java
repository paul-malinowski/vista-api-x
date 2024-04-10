package gov.va.octo.vista.api.dao.jpa;

import javax.ejb.Stateless;
import gov.va.octo.vista.api.dao.AuthAppConfigDao;
import gov.va.octo.vista.api.model.AuthAppConfig;

@Stateless
public class AuthAppConfigDaoImpl extends BaseVistaApiEntityDaoJpa<AuthAppConfig, Long>
        implements AuthAppConfigDao {

}
