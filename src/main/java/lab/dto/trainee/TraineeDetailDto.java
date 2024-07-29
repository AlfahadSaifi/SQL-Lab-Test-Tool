package lab.dto.trainee;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Setter
@Getter
@NoArgsConstructor
@ToString
public class TraineeDetailDto {
        private int id;
        private String employeeId;
        private String designation;
//        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private Date doj;
        private int grade;
        private String ibu;
        private String function;
        private int tierCategorization;
        private int probationPeriod;
        private String personalEmailId;
        private String contactNumber;
//        @DateTimeFormat(pattern = "yyyy-MM-dd")
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
//        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private Date confirmationDate;
}
