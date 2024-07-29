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
public class BatchLabReportPayLoad {
    private int batch;
    private int labId;
    private String traineeId;
    @Enumerated(EnumType.STRING)
    private LabStatus labStatus;
    private String traineeName;
    private String labName;
    private BatchDto batchDto;
    private double labScore;

    private int totalTrainee;
    private int totalLabs;
    private int totalLabTests;

    private List<LabPayload> labPayloadList;
    private List<LabTestPayload> labTestPayloadList;



}
