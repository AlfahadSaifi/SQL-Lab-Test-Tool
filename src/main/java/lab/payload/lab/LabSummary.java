package lab.payload.lab;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class LabSummary {
    private int reportId;
    private int labId;
    private String traineeName;
    private String labName;
    private String traineeId;
    private int batchId;
    public LabSummary(int labId, String labName, String traineeName, String traineeId, int batchId) {
        this.labId = labId;
        this.labName = labName;
        this.traineeName = traineeName;
        this.traineeId = traineeId;
        this.batchId = batchId;
    }
}
