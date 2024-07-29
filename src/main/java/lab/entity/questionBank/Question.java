package lab.entity.questionBank;
import lab.entity.schema.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "sql_question_answer_bank_17239")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DynamicInsert
@DynamicUpdate
public class Question{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int questionId;
    @Column(length = 400)
    private String questionDescription;
    @Column(length = 1000)
    private String questionAnswer;
    private int questionPoints;

    private String topic; // new addition

    @Enumerated(EnumType.STRING)
    private DeleteStatus deleteStatus; // new addition

    @Enumerated(EnumType.STRING)
    private QuestionDifficulty questionDifficulty; // new addition

    @ManyToOne(fetch = FetchType.EAGER)
    private Schema schema; // new addition
}