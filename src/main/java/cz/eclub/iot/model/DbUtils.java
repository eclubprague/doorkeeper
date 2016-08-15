package cz.eclub.iot.model;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FlushModeType;
import javax.persistence.Persistence;

public enum DbUtils {
    INSTANCE;

    private EntityManager em;
    private EntityManagerFactory emf;

    private EntityManagerFactory createEntityManagerFactory() {
        emf = Persistence.createEntityManagerFactory("cz.eclub.iot.server.db");
        em = emf.createEntityManager();
        return emf;
    }

    public EntityManager getEntityManager() {
        if (em == null || !em.isOpen()) createEntityManagerFactory();
        em.setFlushMode(FlushModeType.COMMIT);
        return em;
    }

    public void closeEntityManagerFactory() {
        if (emf.isOpen()) emf.close();
    }


    public static DbUtils getInstance() {
        return INSTANCE;
    }


}
