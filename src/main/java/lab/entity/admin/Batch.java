package lab.entity.admin;
import lab.dto.lab.LabDto;
import lab.dto.lab.LabTestDto;
import lab.dto.trainee.TraineeDto;
import lab.entity.lab.Lab;
import lab.entity.lab.LabTest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lab.entity.trainee.Trainee;
import lombok.ToString;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = "sql_batch_17239")
@ToString
@DynamicUpdate
@DynamicInsert
public class Batch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String batchCode;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Trainee> trainees;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Lab> labs;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<LabTest> labTests;
    private Date enrollmentDate;
    private Date enrollmentExpiryDate;
}
