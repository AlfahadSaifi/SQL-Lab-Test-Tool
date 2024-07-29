package lab.payload.batch;

import lab.dto.admin.BatchDto;
import lab.entity.lab.LabStatus;
import lab.payload.lab.LabPayload;
import lab.payload.labTest.LabTestPayload;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class BatchLabTestReportPayload {

    private int batch;
    private int labTestId;
    private String traineeId;
    @Enumerated(EnumType.STRING)
    private LabStatus labTestStatus;
    private String traineeName;
    private String labTestName;
    private BatchDto batchDto;
    private double percentage;
    private String result;
    private int totalTrainee;
    private int totalLabs;
    private int totalLabTests;

    private List<LabPayload> labPayloadList;
    private List<LabTestPayload> labTestPayloadList;
}
