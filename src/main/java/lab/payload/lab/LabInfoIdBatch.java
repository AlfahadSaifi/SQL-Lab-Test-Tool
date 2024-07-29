package lab.payload.lab;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class LabInfoIdBatch {
    private int labInfoId;
    private int batch;
    private String labName;
}
