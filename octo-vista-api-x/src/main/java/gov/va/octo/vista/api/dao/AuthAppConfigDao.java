package gov.va.octo.vista.api.dao;

import javax.ejb.Local;
import gov.va.med.lom.jpa.foundation.dao.BaseEntityDao;
import gov.va.octo.vista.api.model.AuthAppConfig;

/**
 * Data access methods for AuthAppConfig
 * 
 * @author william.mccarty@va.gov
 *
 */
@Local
public interface AuthAppConfigDao extends BaseEntityDao<AuthAppConfig, Long> {

}
