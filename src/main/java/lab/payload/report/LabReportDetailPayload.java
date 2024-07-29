package lab.payload.report;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
@Setter
@Getter
@NoArgsConstructor
@ToString
public class LabReportDetailPayload {
    private String traineeId;
    private String traineeName;
    private String batchCode;
    private String labName;
    private double totalScore;
    private double gotScore;
    private int totalLabQuestions;
    private int totalSkipQuestions;
    private int totalCorrectQuestions;
    private int totalIncorrectQuestions;
    private double percentage;
    private String result;
    private List<DetailLabReport> detailReportPayloads;
}
