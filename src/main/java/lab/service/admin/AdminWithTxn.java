package lab.service.admin;

import lab.dto.admin.AdminDto;
import lab.exceptions.user.UserAlreadyExist;
import org.springframework.web.multipart.MultipartFile;
import lab.dto.questionBank.QuestionDto;
import lab.dto.trainee.TraineeDto;

import java.util.List;

public interface AdminWithTxn {

    boolean uploadFile(List<TraineeDto> traineeDtos,int batchId);
    TraineeDto registerTrainee(TraineeDto traineeDto, int batchId);

    String processExcelTrainee(MultipartFile file, int batchId);

    boolean addQuestion(QuestionDto questionDto, int labId);

    String addQuestion(MultipartFile file, int labId);

    void registerAdmin(AdminDto admin) throws UserAlreadyExist;

    boolean editQuestion(QuestionDto questionDto, int labId, int questionId);

    QuestionDto getQuestion(int labId,int questionId);

    boolean deleteQuestion(int labId,int questionId);

    boolean addQuestionInLabTest(QuestionDto questionDto, int labId);

    String addQuestionInLabTestViaExcel(MultipartFile file, int labTestId);
}
