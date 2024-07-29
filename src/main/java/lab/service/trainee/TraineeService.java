package lab.service.trainee;

import lab.dto.admin.BatchDto;
import lab.dto.lab.LabDto;
import lab.dto.trainee.TraineeDto;
import lab.entity.evaluation.RecordLabAttempt;
import lab.entity.evaluation.RecordLabTestAttempt;
import lab.payload.report.TraineePayload;
import lab.payload.report.TraineeReportPayload;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
public interface TraineeService {

    //to register trainee
    boolean registerTrainee(TraineeDto traineeDto,int batchId);

    TraineeDto getTraineeByUserName(String username);
    TraineeDto getTraineeByEmployeeId(String employeeId);

    List<LabDto> getTraineeLab(String userName);

    void registerTraineeViaExcel(MultipartFile file, int batchId);

    void registerNewTrainee(TraineeDto traineeDto, int batchId);

    void uploadFile(List<TraineeDto> traineeDto,int batchId);

    double getTraineeLabObtainedPoint(List<RecordLabAttempt> recordLabAttemptList);

    double getTraineeLabTotalMarks(int labId);

    List<String> getTraineeIdByBatchId(int batchId);

    int getBatchIdByUserName(String traineeId);

    double getTraineeLabTestObtainedPoint(List<RecordLabTestAttempt> recordLabTestAttemptList);

    double getTraineeLabTestTotalPoints(int labTestId);

    List<TraineeReportPayload> getTraineeReports(int batchId);

    List<TraineePayload> getAllTrainee();

    void changePassword(String newPassword, String employeeId);
}
