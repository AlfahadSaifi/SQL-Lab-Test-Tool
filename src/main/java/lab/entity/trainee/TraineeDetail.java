package lab.entity.trainee;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;
@Setter
@Getter
@NoArgsConstructor
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = "sql_trainee_detail_17239")
@ToString
@DynamicInsert
@DynamicUpdate
public class TraineeDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String employeeId;
    private String designation;
    private Date doj;
    private int grade;
    private String ibu;
    private String function;
    private int tierCategorization;
    private int probationPeriod;
    private String personalEmailId;
    private String contactNumber;
    private Date dob;
    private String io;
    private double tenthPercent;
    private double twelfthPercent;
    private String graduation;
    private double graduationPercent;
    private String branch;
    private int graduationYOP;
    private String collegeName;
    private String university;
    private String universityShortName;
    private String address;
    private String city;
    private String state;
    private String country;
    private int pinCode;
    private String userId;
    private Date confirmationDate;
}
