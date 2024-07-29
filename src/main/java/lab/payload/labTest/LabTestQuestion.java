package lab.payload.labTest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Setter
@Getter
@NoArgsConstructor
@ToString
public class LabTestQuestion {
    private int correctLabTestQuestion;
    private int incorrectLabTestQuestion;
    private int unattemptedLabTestQuestion;
}