package lab.payload.batch;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class BatchIdCode {
    private int id;
    private String batchCode;
    public BatchIdCode(int id, String batchCode) {
        this.id = id;
        this.batchCode = batchCode;
    }
}
