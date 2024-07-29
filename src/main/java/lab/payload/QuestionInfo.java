package lab.payload;

import lab.dto.lab.LabTestDto;
import lab.entity.lab.LabTestInfo;
import lab.entity.timer.Timer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class QuestionInfo {
    private LabTestDto labTestDto;
    private LabTestInfo labTestInfo;
    private Timer timer;
}
