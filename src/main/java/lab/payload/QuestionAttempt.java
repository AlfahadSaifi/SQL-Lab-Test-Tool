package lab.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class QuestionAttempt {
   private int traineeId;
   private int totalQuestion;
   private int totalSkipQuestion;
   private int totalCorrectQuestion;
   private int totalInCorrectQuestion;
}
