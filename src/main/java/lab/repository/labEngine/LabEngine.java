package lab.repository.labEngine;

import lab.payload.answer.AnswerResponseMessage;

public interface LabEngine {
    void setSubmittedQuery(String submittedQuery);

    void setReferenceQuery(String referenceQuery);

    AnswerResponseMessage checkAns();
}
