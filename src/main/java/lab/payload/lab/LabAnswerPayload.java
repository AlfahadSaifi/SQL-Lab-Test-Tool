package lab.payload.lab;

import lab.entity.lab.LabInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Setter
@Getter
@NoArgsConstructor
@ToString
public class LabAnswerPayload {
    private int labId;
    private int questionId;
    private String query;
    private LabInfo labInfo;
}

