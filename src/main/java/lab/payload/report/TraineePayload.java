package lab.payload.report;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class TraineePayload {

    private String traineeId;
    private String traineeName;
    private String batchCode;
    private int batchId;

    public TraineePayload(String traineeId, String traineeName, String batchCode, int batchId) {
        this.traineeId = traineeId;
        this.traineeName = traineeName;
        this.batchCode = batchCode;
        this.batchId = batchId;
    }
}
