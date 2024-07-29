package lab.payload.batch;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class BatchStatusPayload {
    public int batchId;
    public String batchCode;
}
