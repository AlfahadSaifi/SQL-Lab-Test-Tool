package lab.entity.evaluation;

import lab.dto.evaluation.TraineeLabReportsDto;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;
import javax.persistence.*;
@Setter
@Getter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "sql_lab_test_reports_17239")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Cacheable
@DynamicInsert
@DynamicUpdate
public class LabTestReports {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int batchId;

    @OneToMany(cascade = CascadeType.ALL)
    List<TraineeLabTestReports> traineeLabTestReports;

}
