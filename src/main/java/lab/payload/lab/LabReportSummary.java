package lab.payload.lab;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class LabReportSummary {

    private int reportId;
    private String employeeId;
    private int batchId;
    private String batchName;
    private String labTestName;
    private String employeeName;
    private int labTestId;
    private int totalQuestions;
    private int correctQuestions;
    private int incorrectQuestions;
    private int skippedQuestions;
    private String result;
    private double percentage;
}