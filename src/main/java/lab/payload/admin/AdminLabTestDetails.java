package lab.payload.admin;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class AdminLabTestDetails {
    private Long noOfUnAssignedLabTest;
    private Long noOfAssignedLabTest;
}
