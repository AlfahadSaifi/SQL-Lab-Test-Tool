package lab.dto.admin;
import lab.dto.lab.LabDto;
import lab.dto.lab.LabTestDto;
import lab.dto.trainee.TraineeDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Date;
import java.util.List;
@Setter
@Getter
@NoArgsConstructor
@ToString
public class BatchDto {
    private int id;
    @NotEmpty
    private String batchCode;
    private List<TraineeDto> trainees;
    private List<LabDto> labs;
    private List<LabTestDto> labTests;
    private Date enrollmentDate;
    private Date enrollmentExpiryDate;
}
