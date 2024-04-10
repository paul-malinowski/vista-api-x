package gov.va.octo.vista.api.dao.jpa;

import javax.ejb.Stateless;

import gov.va.octo.vista.api.dao.AuthPermissionDao;
import gov.va.octo.vista.api.model.AuthPermission;

@Stateless
public class AuthPermissionDaoImpl extends BaseVistaApiEntityDaoJpa<AuthPermission, Long>
        implements AuthPermissionDao {

}
