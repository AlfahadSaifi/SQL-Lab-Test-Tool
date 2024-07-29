package lab.payload.lab;

import lab.dto.lab.LabDto;
import lab.entity.lab.LabInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class QuestionLabInfo {
    private LabDto labDto;
    private LabInfo labInfo;
}
