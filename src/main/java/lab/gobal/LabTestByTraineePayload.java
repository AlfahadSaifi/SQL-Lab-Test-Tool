package lab.gobal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class LabTestByTraineePayload {
    private int labTestId;
    private String labTestName;
    private double obtainedMarks;
    private double passPercentage;
    private String traineeId;
    private String labTestStatus;
    private int totalLabTestPoints;
}
