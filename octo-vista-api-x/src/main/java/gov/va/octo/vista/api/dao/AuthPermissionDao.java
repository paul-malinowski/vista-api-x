package gov.va.octo.vista.api.dao;

import javax.ejb.Local;

import gov.va.med.lom.jpa.foundation.dao.BaseEntityDao;
import gov.va.octo.vista.api.model.AuthPermission;

/**
 * Data access methods for AuthPermission
 * 
 * @author william.mccarty@va.gov
 *
 */
@Local
public interface AuthPermissionDao extends BaseEntityDao<AuthPermission, Long> {

}
