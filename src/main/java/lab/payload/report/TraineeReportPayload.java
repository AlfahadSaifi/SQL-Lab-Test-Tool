package lab.payload.report;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class TraineeReportPayload {
    LabTestReport labTestReports;
    LabReport labReports;
    double obtainedScore;
    double totalScore;
    private String traineeId;
    private String traineeName;
    private String batchCode;
    private String labName;
    private String labTestName;
    private String labTestResult;
    private double percentage;

    public TraineeReportPayload(String traineeId, String traineeName) {
        this.traineeId = traineeId;
        this.traineeName = traineeName;
    }
}
