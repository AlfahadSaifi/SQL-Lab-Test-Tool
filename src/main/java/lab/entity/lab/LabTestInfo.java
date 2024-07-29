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
@ToString
@Entity
@Table(name = "sql_lab_test_info_17239")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DynamicInsert
@DynamicUpdate
public class LabTestInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int labTestInfoId;
    private int batch;
    private int labTestId;
    private String traineeId;
    @Enumerated(EnumType.STRING)
    private LabStatus labTestStatus;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<QuestionStatus> questionStatusList;
}