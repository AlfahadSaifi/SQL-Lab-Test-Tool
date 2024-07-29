package lab.payload.answer;

import lab.entity.lab.LabTestInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class AnswerPayload {
    private int labId;
    private int questionId;
    private String query;
    private LabTestInfo labTestInfo;
}
