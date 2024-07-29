package lab.dto.trainee;
import lab.dto.user.UserDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class TraineeDto extends UserDto {
    private int batchId;
    private TraineeDetailDto traineeDetail;
}