package lab.entity.lab;
import lab.entity.admin.Admin;
import lab.entity.evaluation.LabTestReports;
import lab.entity.schema.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lab.entity.questionBank.Question;
import lombok.ToString;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Cacheable
@ToString
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "sql_lab_test_17239")
@DynamicInsert
@DynamicUpdate
public class LabTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int labTestId;

    private String labTestName;

    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Question> questions;

//    private byte[] referenceFile;

    @OneToMany(cascade = CascadeType.ALL) // need to be reviewed
    private List<LabTestReports> labTestReports;

    @Column(name = "created_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime createdDate;

    @Column(name = "start_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime startDate;

    @Column(name = "end_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime endDate;

    @Column(name = "duration")
    private int duration;
    @Column(name = "pass_percentage")
    private double passPercentage;
    private double negativeMarkingFactor;
    @Column(name = "attempts_per_question")
    private int attemptsPerQuestion;
    @Column(name = "points_per_question")
    private int pointsPerQuestion;

    @Column(name = "assigned_by")
    private String assignedBy;

}
