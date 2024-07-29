package lab.payload.answer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class AnswerResponseMessage {
    private boolean isCorrect;
    private boolean isIncorrect;
    private String isInvalidSyntax;
    private java.util.List output = new ArrayList();
}
