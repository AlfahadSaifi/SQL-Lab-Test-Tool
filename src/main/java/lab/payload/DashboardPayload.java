package lab.payload;

import lab.payload.lab.LabDetail;
import lab.payload.lab.LabQuestion;
import lab.payload.labTest.LabTestDetail;
import lab.payload.labTest.LabTestQuestion;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Setter
@Getter
@NoArgsConstructor
@ToString
public class DashboardPayload {
    private LabDetail labDetail;
    private LabQuestion labQuestion;
    private LabTestDetail labTestDetail;
    private LabTestQuestion labTestQuestion;
}