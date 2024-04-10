package gov.va.octo.vista.api.dao.jpa;

import javax.ejb.Stateless;

import gov.va.octo.vista.api.dao.AuthStationDao;
import gov.va.octo.vista.api.model.AuthStation;

@Stateless
public class AuthStationDaoImpl extends BaseVistaApiEntityDaoJpa<AuthStation, Long>
        implements AuthStationDao {

}
