package lab.payload.batch;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class BatchInfo {
    private int id;
    private String batchCode;
    private int totalTrainees;
    private int totalLabs;
    private int totalLabTests;
}
