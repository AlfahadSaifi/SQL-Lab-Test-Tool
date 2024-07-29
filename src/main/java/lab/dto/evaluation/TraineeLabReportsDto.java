package lab.dto.evaluation;

import lab.dto.trainee.TraineeDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.List;

@ToString
@NoArgsConstructor
@Setter
@Getter
public class TraineeLabReportsDto {
    private int id;
    private TraineeDto trainee;
    private double totalPoints;
    private List<RecordLabAttemptDto> recordAttempt;
}
