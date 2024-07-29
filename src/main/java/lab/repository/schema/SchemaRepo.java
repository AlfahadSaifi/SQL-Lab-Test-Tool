package lab.repository.schema;
// author - Sarthak & Ashish

import lab.dto.schema.SchemaDTO;

import java.util.List;

public interface SchemaRepo {
    List<SchemaDTO> fetch();

    SchemaDTO fetchById(int id);

    int insertSchema(SchemaDTO schemaDTO);
}
