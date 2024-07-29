package lab.payload.report;

import lab.entity.lab.LabTestInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class LabTestReport {
    private LabTestInfo labTestInfo;
    private String traineeName;
    private double passPercentage;
}
