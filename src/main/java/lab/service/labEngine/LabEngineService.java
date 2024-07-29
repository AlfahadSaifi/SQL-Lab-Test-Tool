package lab.service.labEngine;
import lab.payload.answer.AnswerResponseMessage;

public interface LabEngineService {
    AnswerResponseMessage submitAns(String id, String query);
}
