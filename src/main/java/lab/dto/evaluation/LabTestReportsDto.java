package lab.dto.evaluation;

import lab.entity.evaluation.TraineeLabReports;
import lab.entity.evaluation.TraineeLabTestReports;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class LabTestReportsDto {

    private int id;
    private int batchId;
    List<TraineeLabTestReportsDto> traineeLabTestReports;
//    private TraineeLabReportsDto traineeLabReports;
}
