package lab.service.trainee;

import lab.dto.admin.BatchDto;
import lab.dto.lab.LabDto;
import lab.dto.trainee.TraineeDto;
import lab.entity.evaluation.RecordLabAttempt;
import lab.entity.evaluation.RecordLabTestAttempt;
import lab.entity.user.Role;
import lab.payload.report.TraineePayload;
import lab.payload.report.TraineeReportPayload;
import lab.repository.batches.BatchRepo;
import lab.repository.trainee.TraineeRepo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TraineeServiceImpl implements TraineeService {

    @Autowired
    private BatchRepo batchRepo;

    @Autowired
    private TraineeRepo traineeRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean registerTrainee(TraineeDto traineeDto, int batchId) {
        String password = traineeDto.getPassword();
        traineeDto.setPassword(passwordEncoder.encode(password));
        traineeDto.setRole(Role.ROLE_TRAINEE);
        traineeDto.setBatchId(batchId);
        BatchDto batchDto = batchRepo.getBatchById(batchId);
        List<TraineeDto> traineeDtos = batchDto.getTrainees();
        traineeDtos.add(traineeDto);
        batchDto.setTrainees(traineeDtos);
        batchRepo.updateBatch(batchDto);
        return true;
    }


    @Override
    public TraineeDto getTraineeByUserName(String username) {
        TraineeDto traineeDto = traineeRepo.getTraineeByUserName(username);
        return traineeDto;
    }
    @Override
    public TraineeDto getTraineeByEmployeeId(String employeeId) {
        TraineeDto traineeDto = traineeRepo.getTraineeByUserName(employeeId);
        return traineeDto;
    }

    @Override
    public void registerTraineeViaExcel(MultipartFile file, int batchId) {
        processExcelTrainee(file,batchId);
    }

    @Override
    public void registerNewTrainee(TraineeDto traineeDto, int batchId) {
        String password = traineeDto.getPassword();
        traineeDto.setPassword(passwordEncoder.encode(password));
        traineeDto.setRole(Role.ROLE_TRAINEE);
        traineeDto.setBatchId(batchId);
        List<TraineeDto> traineeDtos = new ArrayList<>();
        traineeDtos.add(traineeDto);
        uploadFile(traineeDtos,batchId);
    }


    public String processExcelTrainee(MultipartFile file, int batchId){
        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook;
            String fileName = file.getOriginalFilename();
            if (fileName != null && fileName.toLowerCase().endsWith(".xls")) {
                workbook = new HSSFWorkbook(inputStream); // For .xls files
            } else if (fileName != null && fileName.toLowerCase().endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(inputStream); // For .xlsx files
            } else {
                return "Unsupported file format.Please upload correct file Format";
            }
            Sheet sheet = workbook.getSheetAt(0);
            List<TraineeDto> traineeDtos = new ArrayList<>();
            Row row;
            for(int i = 1;i<=sheet.getLastRowNum();i++){
                row = (Row) sheet.getRow(i);
                Cell empId = row.getCell(0);
                Cell name = row.getCell(1);
                Cell password = row.getCell(2);
                Cell email = row.getCell(3);
                TraineeDto traineeDto = new TraineeDto();
                int empId1  = (int)empId.getNumericCellValue();
                traineeDto.setEmployeeId(String.valueOf(empId1));
                traineeDto.setName(String.valueOf(name));
                traineeDto.setEmailId(String.valueOf(email));
                traineeDto.setPassword(passwordEncoder.encode(String.valueOf(password)));
                traineeDto.setRole(Role.ROLE_TRAINEE);
                traineeDto.setBatchId(batchId);
//                traineeDto.setBatchDto(batchDto);
                traineeDtos.add(traineeDto);
            }
            //call to save the list into the table
            if(!traineeDtos.isEmpty()){
                uploadFile(traineeDtos,batchId);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Unexcepted Error";
    }

    public void uploadFile(List<TraineeDto> traineeDtos, int batchId) {
        BatchDto batchDto = batchRepo.getBatchById(batchId);
        List<TraineeDto> traineeList = batchDto.getTrainees();
        traineeList.addAll(traineeDtos);
        batchDto.setTrainees(traineeList);
        batchRepo.updateBatch(batchDto);
    }

    @Override
    public double getTraineeLabObtainedPoint(List<RecordLabAttempt> recordLabAttemptList) {
        double totalPoints = 0;
        for (RecordLabAttempt recordLabAttempt:recordLabAttemptList){
            totalPoints += recordLabAttempt.getTraineeCurrentQuestionPoints();
        }
        return totalPoints;
    }

    @Override
    public double getTraineeLabTotalMarks(int labId) {
        return traineeRepo.getTraineeLabTotalMarks(labId);
    }

    @Override
    public List<String> getTraineeIdByBatchId(int batchId) {
        return traineeRepo.getTraineeIdByBatchId(batchId);
    }

    @Override
    public int getBatchIdByUserName(String traineeId) {
        return traineeRepo.getBatchIdByUserName(traineeId);
    }

    @Override
    public double getTraineeLabTestObtainedPoint(List<RecordLabTestAttempt> recordLabTestAttemptList) {
        double totalPoints = 0;
        for (RecordLabTestAttempt recordLabTestAttempt:recordLabTestAttemptList){
            totalPoints += recordLabTestAttempt.getTraineeCurrentQuestionPoints();
        }
        return totalPoints;
    }

    @Override
    public double getTraineeLabTestTotalPoints(int labTestId) {
        return traineeRepo.getTraineeLabTestTotalPoints(labTestId);
    }

    @Override
    public List<TraineeReportPayload> getTraineeReports(int batchId) {
        return traineeRepo.getTraineeReportByBatchId(batchId);
    }

    @Override
    public List<TraineePayload> getAllTrainee() {
        return traineeRepo.getAllTrainee();
    }

    @Override
    public void changePassword(String newPassword, String employeeId) {
        String encoderedPassword=passwordEncoder.encode(newPassword);
        traineeRepo.changePassword(encoderedPassword,employeeId);
    }


    @Override
    public List<LabDto> getTraineeLab(String userName) {
//        traineeRepo;
//        TraineeDto traineeDto = traineeRepo.getTraineeByUserName(userName);
//        return traineeDto.getLabs();
        return null;
    }



}
