package lab.payload.labTest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Setter
@Getter
@NoArgsConstructor
@ToString
public class LabTestDetail {
    private int unattemptedLabTest;
    private int resumeLabTest;
    private int completedLabTest;
}