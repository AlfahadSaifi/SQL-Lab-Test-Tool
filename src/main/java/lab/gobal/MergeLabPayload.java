package lab.gobal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class MergeLabPayload {
        private int labId;
        private String labName;
        private int noOfQuestions;
        private int totalMarks;
        private String status;
        private double obtainedMarks;
        private String traineeId;
}
