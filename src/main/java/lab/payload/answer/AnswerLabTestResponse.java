package lab.payload.answer;

import lab.dto.evaluation.RecordLabTestAttemptDto;
import lab.entity.lab.LabTestInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class AnswerLabTestResponse {
    private String output;
    private List outputList;
    private RecordLabTestAttemptDto recordLabTestAttemptDto;
    private LabTestInfo labTestInfo;
}

