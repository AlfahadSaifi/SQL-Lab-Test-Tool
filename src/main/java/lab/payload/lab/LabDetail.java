package lab.payload.lab;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Setter
@Getter
@NoArgsConstructor
@ToString
public class LabDetail {
    private int unattemptedLab;
    private int resumeLab;
    private int completedLab;
}