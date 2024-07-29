package lab.gobal;

import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class HibernateUtil {
    public static String getTableName(Class<?> entityClass, SessionFactory sessionFactory) {
        ClassMetadata classMetadata = sessionFactory.getClassMetadata(entityClass);
        if (classMetadata != null) {
            return classMetadata.getEntityName();
        }
        return null;
    }
}
