package lab.entity.trainee;

import lab.entity.admin.Batch;
import lab.entity.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lab.entity.lab.Lab;
import lab.entity.lab.LabTest;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "trainee_sql_17239")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DynamicInsert
@DynamicUpdate
public class Trainee extends User {
    private int batchId;
    @OneToOne(cascade = CascadeType.ALL)
    private TraineeDetail traineeDetail;
}