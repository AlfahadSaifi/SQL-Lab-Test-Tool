package lab.entity.schema;

import lab.entity.questionBank.Question;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@ToString
@Table(name = "schema_17239")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DynamicInsert
@DynamicUpdate
public class Schema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int schemaReferenceId;
    private String schemaName;
    @Lob
    private byte[] referenceFile;
}
