package lab.payload.report;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class TraineeLabMarksPayload {
    private int labId;
    private double obtainedMarks;
    private String traineeId;
}
