package lab.repository.schema;
// author - Sarthak & Ashish


import lab.convert.Convert;
import lab.dto.schema.SchemaDTO;
import lab.entity.schema.Schema;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Repository

public class SchemaRepoImpl implements SchemaRepo{
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<SchemaDTO> fetch() {
        Session session = sessionFactory.getCurrentSession();
        String hql = "from Schema";
        List<Schema> list= session.createQuery(hql, Schema.class).list();
        List<SchemaDTO> dtoList = Convert.getModelMapper().toDtoList(list, SchemaDTO.class);
        System.out.println("Schema list = "+dtoList);
        return dtoList;
    }

    @Override
    public SchemaDTO fetchById(int id) {
            Session session = sessionFactory.getCurrentSession();
            Schema schema = session.get(Schema.class,id);
            SchemaDTO schemaDTO = Convert.getModelMapper().toDto(schema, SchemaDTO.class);
        return schemaDTO;
    }

    @Override
    public int insertSchema(SchemaDTO schemaDTO){
        Session session = sessionFactory.getCurrentSession();
        Schema schema = Convert.getModelMapper().toEntity(schemaDTO, Schema.class);
        System.out.println("Schema inside DAO-->"+schema);
        int referenceId = -1;
        try {
            referenceId = (int) session.save(schema);
        }
        catch (Exception e)
        {
            e.getMessage();
        }

        return referenceId;
    }
}
