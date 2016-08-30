package cz.eclub.iot.model.DAO;

import cz.eclub.iot.model.DbUtils;
import cz.eclub.iot.model.classes.AbstractEntity;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

public class AbstractDao<T extends AbstractEntity> implements IAbstractDao<T>, Serializable {
    private EntityManager entityManager;
    protected Class<T> persistentClass;

    public AbstractDao() {
        entityManager = DbUtils.getInstance().getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        persistentClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected EntityManager getEntityManager() {
        if (!entityManager.isOpen()) {
            entityManager = DbUtils.getInstance().getEntityManager();
        }
        return entityManager;
    }

    protected void closeEntityManager() {
        if (!entityManager.isOpen()) {
            entityManager.flush();
            entityManager.close();
        }
    }


    @Override
    public boolean addNew(T entity) { //TODO should throw exception
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(entity);
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            getEntityManager().getTransaction().rollback();
            return false;
        } finally {
            closeEntityManager();
        }
    }

    @Override
    public Collection<T> getAll() {
        CriteriaQuery q = getEntityManager().getCriteriaBuilder().createQuery(persistentClass);
        Root<T> abstractRoot = q.from(persistentClass);
        q.select(abstractRoot);

        Collection<T> collection = getEntityManager().createQuery(q).getResultList();
        closeEntityManager();
        return collection;
    }

    @Override
    public boolean exists(T entity) {  //TODO should throw exception
        CriteriaQuery q = getEntityManager().getCriteriaBuilder().createQuery(persistentClass);
        Root<T> abstractRoot = q.from(persistentClass);
        q.select(abstractRoot);
        ParameterExpression<Integer> p = getEntityManager().getCriteriaBuilder().parameter(Integer.class);

        q.where(getEntityManager().getCriteriaBuilder().equal(abstractRoot.get("id"), p));
        Query qq = getEntityManager().createQuery(q);
        return !qq.setParameter(p, entity.getId()).getResultList().isEmpty();
    }

    @Override
    public T getById(int id) {
        CriteriaQuery q = getEntityManager().getCriteriaBuilder().createQuery(persistentClass);
        Root<T> abstractRoot = q.from(persistentClass);
        q.select(abstractRoot);
        ParameterExpression<Integer> p = getEntityManager().getCriteriaBuilder().parameter(Integer.class);

        q.where(getEntityManager().getCriteriaBuilder().equal(abstractRoot.get("id"), p));
        Query qq = getEntityManager().createQuery(q);
        List<T> resultList = qq.setParameter(p, id).getResultList();
        return (resultList.isEmpty()) ? null : resultList.get(0);
    }

    @Override
    public boolean update(T entity) { //TODO should throw exception
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(entity);
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        } finally {
            closeEntityManager();
        }
    }

    @Override
    public boolean delete(T entity) { //TODO should throw exception
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(entity);
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        } finally {
            closeEntityManager();
        }
    }
}
