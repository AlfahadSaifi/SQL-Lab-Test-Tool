package lab.entity.lab;


import lab.entity.evaluation.LabReports;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lab.entity.questionBank.Question;
import lombok.ToString;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;
import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity
@ToString
@Table(name = "sql_lab_17239")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DynamicInsert
@DynamicUpdate
public class Lab {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int labId;

    private String labName;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Question> questions;

    private byte[] referenceFile;
    private double negativeMarkingFactor;
    private int pointsPerQuestion;
    private int attemptPerQuestion;

    @OneToMany(cascade = CascadeType.ALL)
    private List<LabReports> reports;
    private boolean isAssigned = false;
}
