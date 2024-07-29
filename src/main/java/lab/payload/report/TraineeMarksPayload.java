package lab.payload.report;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class TraineeMarksPayload {
    private int labTestId;
    private double obtainedMarks;
    private String traineeId;
}
