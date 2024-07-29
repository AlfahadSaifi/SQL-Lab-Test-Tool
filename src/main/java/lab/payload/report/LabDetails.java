package lab.payload.report;

import lab.entity.lab.LabStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Setter
@Getter
@NoArgsConstructor
@ToString
public class LabDetails {
    private String traineeId;
    private String traineeName;
    private LabStatus labStatus;
    private double obtainedScore;
}