package lab.payload.labTest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class LabTestStatusPayload {
    private int labTestId;
    private String labTestName;
}
