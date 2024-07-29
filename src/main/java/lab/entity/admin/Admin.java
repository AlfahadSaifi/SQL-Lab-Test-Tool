package lab.entity.admin;
import lab.entity.lab.LabTest;
import lab.entity.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lab.entity.lab.Lab;
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
@Table(name = "nsbt_sql_17239")
@Cacheable
@DynamicInsert
@DynamicUpdate
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Admin extends User {

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Batch> batches;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Lab> labs;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<LabTest> labTests;
}