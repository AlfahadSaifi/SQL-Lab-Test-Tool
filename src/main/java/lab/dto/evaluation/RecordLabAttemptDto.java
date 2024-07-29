package lab.dto.evaluation;
import lombok.*;


import java.util.List;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RecordLabAttemptDto {

    private int id;
    private String traineeId;
    private int labId;
    private int questionId;
    private int incorrectAttempt;
    private double traineeCurrentQuestionPoints;
    private List<LabSubmitQueryDto> labSubmitQueries;
}
