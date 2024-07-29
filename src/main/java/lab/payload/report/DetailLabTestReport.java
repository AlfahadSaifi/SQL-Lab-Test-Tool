package lab.payload.report;

import lab.entity.evaluation.LabTestSubmitQuery;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class DetailLabTestReport {
    private int questionId;
    private String questionDescription;
    private double questionPoints;
    private double traineeCurrentQuestionPoints;
    private List<LabTestSubmitQuery> labSubmitQueries;
}
