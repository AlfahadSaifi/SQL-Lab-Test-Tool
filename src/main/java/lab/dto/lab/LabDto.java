package lab.dto.lab;
import lab.dto.evaluation.LabReportsDto;
import lab.dto.questionBank.QuestionDto;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;
@Setter
@Getter
@NoArgsConstructor
@ToString
public class LabDto {
    private int labId;
    @NotNull(message = "Required")
    private String labName;
//    private byte[] referenceFile;
    private List<QuestionDto> questions;
    private int pointsPerQuestion;
    private int attemptPerQuestion;
    private double negativeMarkingFactor;
    private List<LabReportsDto> reports;
    private boolean isAssigned;
}
