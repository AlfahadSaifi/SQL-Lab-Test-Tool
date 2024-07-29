package lab.payload.lab;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class LabPayload {
    private int labId;
    private String labName;
    private int noOfQuestions;
    private long totalMarks;

    public LabPayload(int labId, String labName) {
        this.labId = labId;
        this.labName = labName;
    }
    public LabPayload(int labId, String labName,long totalMarks) {
        this.labId = labId;
        this.labName = labName;
        this.totalMarks = totalMarks;
    }
}
