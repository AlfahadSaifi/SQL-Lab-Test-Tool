package lab.payload;

import lab.entity.evaluation.RecordLabTestAttempt;
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
public class ReportLabTestPayload {
   private List<RecordLabTestAttempt> recordLabTestAttemptList;
   private LabTestInfo labTestInfo;
   private double obtainedPoint;
   private double totalPoints;
}
