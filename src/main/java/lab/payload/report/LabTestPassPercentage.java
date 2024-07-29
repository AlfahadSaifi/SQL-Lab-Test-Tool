package lab.payload.report;

import lab.entity.lab.LabStatus;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class LabTestPassPercentage {
    private int batchId;
    private int labTestId;
    private String traineeId;
    private LabStatus labTestStatus;
    private String traineeName;
    private double passPercentage;
}
