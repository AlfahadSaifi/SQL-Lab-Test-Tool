package lab.payload.report;
import lab.entity.lab.LabInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Setter
@Getter
@NoArgsConstructor
@ToString
public class LabReport {
    private LabInfo labInfo;
    private String traineeName;
}