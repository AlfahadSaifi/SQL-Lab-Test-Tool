package lab.repository.admin;

import lab.dto.admin.AdminDto;
import lab.dto.admin.BatchDto;
import lab.dto.lab.LabDto;
import lab.dto.lab.LabTestDto;
import lab.dto.questionBank.QuestionDto;
import lab.dto.trainee.TraineeDetailDto;
import lab.dto.trainee.TraineeDto;
import lab.dto.user.UserDto;
import lab.exceptionHandler.CustomDatabaseException;
import lab.exceptions.user.UserAlreadyExist;

import java.util.List;

public interface AdminRepo {
    boolean saveTrainee(List<TraineeDto> studentDtos);

    TraineeDto saveTrainee(TraineeDto traineeDto);

    boolean addQuestion(QuestionDto questionDto);

    boolean addQuestion(List<QuestionDto> questionDtos);

    void saveAdmin(AdminDto admin) throws UserAlreadyExist;

    AdminDto getAdminByUserName(String username);

    AdminDto updateAdmin(AdminDto adminDto) throws CustomDatabaseException;

    List<BatchDto> getBatchDtoList(String userName);

    List<LabDto> getLabDtoList(String adminUsername);

    LabDto addLabToAdmin(LabDto labDto, String adminId);

    LabTestDto addLabTestInAdmin(LabTestDto labTestDto, String adminId);

    long getAdminIdByUserName(String userId);

    UserDto createTraineeInUser(UserDto userDto);

    void insertIntoTrainee(Long id, int batchId);

    TraineeDetailDto getTranieeDetailByEmpId(String employeeId);

    TraineeDetailDto editTranieeDetailByEmpId(TraineeDetailDto traineeDetailDto, String employeeId);

    TraineeDetailDto editTranieeDetailById(TraineeDetailDto traineeDetailDto);

    void changePassword(String newPassword, String employeeId);

    boolean removeLab(int labId, String userName);

    boolean removeLabTest(int labTestId, String userName);
}
