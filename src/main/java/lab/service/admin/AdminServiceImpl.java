package lab.service.admin;

import lab.dto.admin.BatchDto;
import lab.dto.trainee.TraineeDetailDto;
import lab.dto.trainee.TraineeDto;
import lab.entity.user.Role;
import lab.entity.user.UserStatus;
import lab.repository.admin.AdminRepo;
import lab.repository.batches.BatchRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepo adminRepo;
    @Autowired
    private BatchRepo batchRepo;

    @Autowired
    private AdminWithTxn adminWithTxn;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String registerTraineeViaExcel(MultipartFile file, int batchId) {
        String output = adminWithTxn.processExcelTrainee(file, batchId);
        return output;
    }
    @Override
    public TraineeDto createTraineeInUser(TraineeDto traineeDto, int batchId, String userId) {
        traineeDto.setPassword(passwordEncoder.encode(traineeDto.getPassword()));
        traineeDto.setUserStatus(UserStatus.ACTIVE);
        traineeDto.setRole(Role.ROLE_TRAINEE);
        traineeDto.getTraineeDetail().setEmployeeId(traineeDto.getEmployeeId());
        BatchDto batchDto = batchRepo.getBatchById(batchId);
        List<TraineeDto> trainees = batchDto.getTrainees();
        trainees.add(traineeDto);
        try {
            batchRepo.updateBatch(batchDto);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return traineeDto;
    }

    @Override
    public TraineeDetailDto getTranieeDetailByEmpId(String employeeId) {
        return adminRepo.getTranieeDetailByEmpId(employeeId);
    }

    @Override
    public TraineeDetailDto editTranieeDetailByEmpId(TraineeDetailDto traineeDetailDto, String employeeId) {
        return adminRepo.editTranieeDetailByEmpId(traineeDetailDto,employeeId);
    }

    @Override
    public TraineeDetailDto editTranieeDetailById(TraineeDetailDto traineeDetailDto) {
        return adminRepo.editTranieeDetailById(traineeDetailDto);
    }

    @Override
    public void changePassword(String newPassword, String employeeId) {
        String encoderedPassword=passwordEncoder.encode(newPassword);
        adminRepo.changePassword(encoderedPassword,employeeId);
    }
}
