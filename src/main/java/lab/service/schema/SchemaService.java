package lab.service.schema;
// author - Sarthak & Ashish

import lab.dto.schema.SchemaDTO;


import java.util.List;


public interface SchemaService {
    List<SchemaDTO> fetchAllSchema();
    SchemaDTO fetchById(int id);
    int insertSchema(SchemaDTO schemaDTO);
}
