package gov.va.octo.vista.api.dao;

import javax.ejb.Local;

import gov.va.med.lom.jpa.foundation.dao.BaseEntityDao;
import gov.va.octo.vista.api.model.AuthStation;

/**
 * Data access methods for AuthStation
 * 
 * @author william.mccarty@va.gov
 *
 */
@Local
public interface AuthStationDao extends BaseEntityDao<AuthStation, Long> {

}
