package lab.service.schema;
// author - Sarthak & Ashish


import lab.dto.schema.SchemaDTO;
import lab.repository.schema.SchemaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SchemaServiceImpl implements SchemaService {

    @Autowired
    SchemaRepo schemaRepo;

    @Override
    public List<SchemaDTO> fetchAllSchema() {
        return schemaRepo.fetch();
    }

    @Override
    public SchemaDTO fetchById(int id) {
        return schemaRepo.fetchById(id);
    }

    public int insertSchema(SchemaDTO schemaDTO){
        return schemaRepo.insertSchema(schemaDTO);
    }
}
