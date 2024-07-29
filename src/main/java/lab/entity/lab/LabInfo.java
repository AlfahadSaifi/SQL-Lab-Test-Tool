package lab.entity.lab;

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
@Table(name = "sql_lab_info_17239")
@Cacheable
@ToString
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DynamicInsert
@DynamicUpdate
public class LabInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int labInfoId;
    private int batch;
    private int labId;
    private String traineeId;
    @Enumerated(EnumType.STRING)
    private LabStatus labStatus;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<QuestionStatus> questionStatusList;
}
