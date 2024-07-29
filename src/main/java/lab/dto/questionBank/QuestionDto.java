package lab.dto.questionBank;

import lab.dto.schema.SchemaDTO;
import lab.entity.questionBank.DeleteStatus;
import lab.entity.questionBank.QuestionDifficulty;
import lab.entity.schema.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class QuestionDto {
    private int questionId;
    private String questionDescription;
    private String questionAnswer;
    private int questionPoints;
    //new additions
    private String topic;
    private DeleteStatus deleteStatus;
    private QuestionDifficulty questionDifficulty;
    private SchemaDTO schema;

}