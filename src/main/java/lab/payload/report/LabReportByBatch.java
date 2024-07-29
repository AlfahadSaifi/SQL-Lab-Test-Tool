package lab.payload.report;
import lab.entity.lab.LabStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.List;
@Setter
@Getter
@NoArgsConstructor
@ToString
public class LabReportByBatch {
    private int labId;
    private int batchId;
    private String batchCode;
    private String labName;
    private double totalScore;
    private List<LabDetails> labDetails;
}