package lab.service.reports;

import lab.dto.evaluation.RecordLabAttemptDto;
import lab.dto.evaluation.RecordLabTestAttemptDto;
import lab.dto.evaluation.TraineeLabReportsDto;
import lab.payload.QuestionAttempt;

import java.util.List;

public interface ReportService {

    int getLabCorrectAns(int reportId);

    int getLabAttemptQuestion(int reportId);

    int getLabIncorrectAns(int reportId);

    int getLabSkipQuestion(int labId, int reportId);

    List<RecordLabAttemptDto> fetchAllReports(int reportId);

    void saveRecordAttempt(RecordLabAttemptDto attemptDto, String userQuery, boolean isCorrect);

    RecordLabAttemptDto fetchRecordAttempt(String userName, int labId, int questionId);

    void submitLab(int labId1, String userName);

    TraineeLabReportsDto getLabTraineeRecord(int reportId);

    RecordLabTestAttemptDto fetchRecordLabTestAttempt(String traineeId, int labTestId, int questionId);

    void saveRecordLabTestAttempt(RecordLabTestAttemptDto recordLabTestAttemptDto, String query, boolean isCorrect);

    void submitLabTest(int labTestId, String userId);

    int getLabTestCount(String userId);

    int getLabCount(String userId);

    int getBatchCount(String userId);

    int getTraineeCount();
}
