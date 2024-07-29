package lab.payload.answer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class QuestionPointsPayLoad {
    private int questionId;
    private int questionPoints;
    private double passPercentage;
    private String questionDescription;
}
