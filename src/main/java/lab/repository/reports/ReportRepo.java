package lab.repository.reports;
import lab.dto.evaluation.RecordLabAttemptDto;
import lab.dto.evaluation.RecordLabTestAttemptDto;
import lab.dto.evaluation.TraineeLabReportsDto;
import lab.dto.lab.LabDto;

import java.util.List;
public interface ReportRepo {

    void saveRecordAttempt(RecordLabAttemptDto recordLabAttemptDto);

    void saveTraineeAttemptRecord(RecordLabAttemptDto attemptDto);
    List<RecordLabAttemptDto> fetchAllReports(int reportId);

    void SubmitLabReports(LabDto labDto);

    RecordLabAttemptDto fetchRecordAttempt(String userName, int labId, int questionId);

    List<RecordLabAttemptDto> fetchRecordAttempt(String traineeId, int labId1);

    TraineeLabReportsDto fetchTraineeReports(int reportId);

    RecordLabTestAttemptDto fetchRecordLabTestAttempt(String traineeId, int labTestId, int questionId);

    void saveRecordLabTestAttempt(RecordLabTestAttemptDto recordLabTestAttemptDto);

    List<RecordLabTestAttemptDto> fetchLabTestRecordAttempt(String userId, int labTestId);

    int getLabTestCount(String userId);

    int getLabCount(String userId);

    int getBatchCount(String userId);

    int getTraineeCount();
}
