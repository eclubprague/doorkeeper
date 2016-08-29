package cz.eclub.iot.model.DAO;

import cz.eclub.iot.model.classes.SensorEntity;

import javax.persistence.Query;
import java.util.Collection;
import java.util.List;

public class SensorDao extends AbstractDao<SensorEntity> {
    public Collection<SensorEntity> getByUUID(String uuid) {
        Query q = getEntityManager().createQuery("select s from SensorEntity as s where s.UUID=:uuid");
        q.setParameter("uuid",uuid);
        List result = q.getResultList();
        closeEntityManager();
        return result;
    }
}
