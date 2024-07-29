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
public class TestDetail {
    private String traineeId;
    private String traineeName;
    private LabStatus labTestStatus;
    private double obtainedScore;
}
