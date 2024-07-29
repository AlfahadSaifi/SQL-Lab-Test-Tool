package lab.dto.evaluation;

import lab.entity.evaluation.TraineeLabReports;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class LabReportsDto {

    private int id;
    private int batchId;
    List<TraineeLabReportsDto> traineeLabReports;
//    private TraineeLabReportsDto traineeLabReports;
}
