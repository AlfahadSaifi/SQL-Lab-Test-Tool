package lab.payload.report;

import lab.entity.lab.LabStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;
@Setter
@Getter
@NoArgsConstructor
@ToString
public class LabTestReportByBatch {
    private int labTestId;
    private int batchId;
    private String batchCode;
    private String labTestName;
    private double totalScore;
    private double passPercentage;
    private List<TestDetail> testDetails;

}
