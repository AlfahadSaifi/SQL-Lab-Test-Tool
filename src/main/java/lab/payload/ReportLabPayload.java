package lab.payload;


import lab.entity.evaluation.RecordLabAttempt;
import lab.entity.lab.LabInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
@Setter
@Getter
@NoArgsConstructor
@ToString
public class ReportLabPayload {
    private List<RecordLabAttempt> recordLabAttemptList;
    private LabInfo labTestInfo;
    private double obtainedPoint;
    private double totalPoints;
}

