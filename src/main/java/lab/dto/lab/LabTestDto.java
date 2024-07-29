package lab.dto.lab;

import java.time.LocalDateTime;
import lab.dto.evaluation.LabReportsDto;
import lab.dto.evaluation.LabTestReportsDto;
import lab.dto.questionBank.QuestionDto;
import lab.entity.questionBank.Question;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class LabTestDto {

    private int labTestId;
    @NotNull(message = "Required")
    private String labTestName;
    private List<QuestionDto> questions;

//    private byte[] referenceFile;

    List<LabTestReportsDto> labTestReports;

    private LocalDateTime createdDate;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private int duration;

    private double passPercentage;

    private int attemptsPerQuestion;
    private int pointsPerQuestion;
    private double negativeMarkingFactor;
    private String assignedBy;

    public LabTestDto(int labTestId, String labTestName, LocalDateTime createdDate, LocalDateTime startDate, LocalDateTime endDate,
                      int duration, double passPercentage, int attemptsPerQuestion, int pointsPerQuestion,
                      double negativeMarkingFactor, String assignedBy) {
        this.labTestId = labTestId;
        this.labTestName = labTestName;
        this.createdDate = createdDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
        this.passPercentage = passPercentage;
        this.attemptsPerQuestion = attemptsPerQuestion;
        this.pointsPerQuestion = pointsPerQuestion;
        this.negativeMarkingFactor = negativeMarkingFactor;
        this.assignedBy = assignedBy;
    }
}
