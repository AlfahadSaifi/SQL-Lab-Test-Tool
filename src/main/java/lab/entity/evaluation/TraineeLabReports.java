package lab.entity.evaluation;


import lab.entity.trainee.Trainee;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;
import javax.persistence.*;


@Setter
@Getter
@NoArgsConstructor
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "sql_trainee_lab_reports_17239")
@Entity
@DynamicInsert
@DynamicUpdate
public class TraineeLabReports {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    private Trainee trainee;
    private double totalPoints;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<RecordLabAttempt> recordAttempt;

}
