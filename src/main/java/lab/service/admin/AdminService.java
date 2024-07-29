package lab.service.admin;

import lab.dto.trainee.TraineeDetailDto;
import lab.dto.trainee.TraineeDto;
import org.springframework.web.multipart.MultipartFile;

public interface AdminService {
    String registerTraineeViaExcel(MultipartFile inputStream, int batchId);

    TraineeDto createTraineeInUser(TraineeDto traineeDto, int batchId, String userId);

    TraineeDetailDto getTranieeDetailByEmpId(String employeeId);

    TraineeDetailDto editTranieeDetailByEmpId(TraineeDetailDto traineeDetailDto, String employeeId);

    TraineeDetailDto editTranieeDetailById(TraineeDetailDto traineeDetailDto);

    void changePassword(String newPassword, String employeeId);
}
