package lab.dto.schema;
// author - Sarthak & Ashish


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@NoArgsConstructor
@ToString
public class SchemaDTO {

    private int schemaReferenceId;
    private String schemaName;
    private byte[] referenceFile;

}
