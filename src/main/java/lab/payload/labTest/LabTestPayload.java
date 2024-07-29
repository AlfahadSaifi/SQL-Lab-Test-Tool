package lab.payload.labTest;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
@Setter
@Getter
@NoArgsConstructor
@ToString
public class LabTestPayload {
    private int labTestId;
    private String labTestName;
    private int noOfQuestions;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int duration;
    private long totalLabTestPoints;
    public LabTestPayload(int labTestId, String labTestName) {
        this.labTestId = labTestId;
        this.labTestName = labTestName;
    }
    public LabTestPayload(int labTestId, String labTestName, long totalLabTestPoints) {
        this.labTestId = labTestId;
        this.labTestName = labTestName;
        this.totalLabTestPoints = totalLabTestPoints;
    }
}
