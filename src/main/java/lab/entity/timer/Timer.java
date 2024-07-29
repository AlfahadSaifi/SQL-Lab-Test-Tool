package lab.entity.timer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@Table(name = "sql_timer_17239")
@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DynamicInsert
@DynamicUpdate
public class Timer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int timerId;
    private int batch;
    private int testId;
    private String traineeId;
    private int timerLeft;

}
