package gov.va.octo.vista.api.dao;

import javax.ejb.Local;

import gov.va.med.lom.jpa.foundation.dao.BaseEntityDao;
import gov.va.octo.vista.api.model.AuthApp;

/**
 * Data access for {@link gov.va.octo.vista.api.model.AuthApp}
 * 
 * @author william.mccarty@va.gov
 *
 */
@Local
public interface AuthAppDao extends BaseEntityDao<AuthApp, Long> {

    /**
     * query: find by key
     */
    public static final String FIND_BY_KEY = "auth.AuthApp.key";

    public AuthApp findByKey(String key);

}
