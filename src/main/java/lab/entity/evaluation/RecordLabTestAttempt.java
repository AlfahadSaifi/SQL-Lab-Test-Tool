package lab.entity.evaluation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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
@Table(name = "sql_record_lab_test_attempt_17239",uniqueConstraints = @UniqueConstraint(columnNames = {"labTestId","traineeId","questionId"}))
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DynamicInsert
@DynamicUpdate
public class RecordLabTestAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String traineeId;
    private int labTestId;
    private int questionId;
    private int incorrectAttempt;
    private double traineeCurrentQuestionPoints;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private  List<LabTestSubmitQuery> labTestSubmitQueries;
}
