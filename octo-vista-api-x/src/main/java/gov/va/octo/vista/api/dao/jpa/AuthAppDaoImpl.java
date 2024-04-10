package gov.va.octo.vista.api.dao.jpa;

import javax.ejb.Stateless;
import gov.va.octo.vista.api.dao.AuthAppDao;
import gov.va.octo.vista.api.model.AuthApp;

@Stateless
public class AuthAppDaoImpl extends BaseVistaApiEntityDaoJpa<AuthApp, Long> implements AuthAppDao {

    /** {@inheritDoc} */
	@Override
    public AuthApp findByKey(String key) {

        String[] paramNames = new String[] {"key"};
        Object[] paramValues = new Object[] {key};

        AuthApp app = fetchUnique(FIND_BY_KEY, paramNames, paramValues);
        if (app == null) {
            // happens if bad key is passed
            return app;
        }
        app.getConfigs().size();

        return app;

    }

}
