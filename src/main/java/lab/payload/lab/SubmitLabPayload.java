package lab.payload.lab;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class SubmitLabPayload {
    private int status;
    private String message;
    private String traineeId;
    private int labId;
}
