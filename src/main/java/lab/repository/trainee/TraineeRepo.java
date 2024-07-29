package lab.repository.trainee;

import lab.dto.trainee.TraineeDto;
import lab.payload.report.TraineePayload;
import lab.payload.report.TraineeReportPayload;

import java.util.List;

public interface TraineeRepo{

    TraineeDto getTraineeByUserName(String username);
    TraineeDto getTraineeByEmployeeId(String employeeId);

    void saveTrainee(TraineeDto traineeDto);

    double getTraineeLabTotalMarks(int labId);

    List<String> getTraineeIdByBatchId(int batchId);

    int getBatchIdByUserName(String traineeId);

    double getTraineeLabTestTotalPoints(int labTestId);

    List<TraineeReportPayload> getTraineeReportByBatchId(int batchId);

    List<TraineePayload> getAllTrainee();

    void changePassword(String encoderedPassword, String employeeId);
}
