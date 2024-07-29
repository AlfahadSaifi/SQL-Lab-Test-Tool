package lab.dto.evaluation;


import lab.entity.evaluation.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class SubmitQueryDto {

    private int id;
    private String querySubmit;
    private Status status;
}
