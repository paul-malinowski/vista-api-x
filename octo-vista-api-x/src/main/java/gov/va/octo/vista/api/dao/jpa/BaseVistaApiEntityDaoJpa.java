package gov.va.octo.vista.api.dao.jpa;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import gov.va.med.lom.jpa.foundation.dao.impl.BaseEntityDaoJpa;

public class BaseVistaApiEntityDaoJpa<T, ID extends Serializable> extends BaseEntityDaoJpa<T, ID> {

    @PersistenceContext(unitName = "vista-api")
    protected EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

}
