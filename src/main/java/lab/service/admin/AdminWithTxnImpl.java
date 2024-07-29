package lab.service.admin;


import lab.dto.admin.AdminDto;
import lab.dto.admin.BatchDto;
import lab.dto.lab.LabDto;
import lab.dto.lab.LabTestDto;
import lab.dto.questionBank.QuestionDto;
import lab.dto.trainee.TraineeDetailDto;
import lab.dto.trainee.TraineeDto;
import lab.entity.user.Role;
import lab.exceptions.user.UserAlreadyExist;
import lab.repository.admin.AdminRepo;
import lab.repository.batches.BatchRepo;
import lab.repository.lab.LabRepo;
import lab.repository.labtest.LabTestRepo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Transactional
@Service
public class AdminWithTxnImpl implements AdminWithTxn {

    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private LabRepo labRepo;
    @Autowired
    private LabTestRepo labTestRepo;

    @Autowired
    private BatchRepo batchRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String processExcelOld(MultipartFile file, int batchId) {
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
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                row = (Row) sheet.getRow(i);
                Cell empId = row.getCell(0);
                Cell name = row.getCell(1);
                Cell password = row.getCell(2);
                Cell email = row.getCell(3);
                TraineeDto traineeDto = new TraineeDto();
                int empId1 = (int) empId.getNumericCellValue();
                traineeDto.setEmployeeId(String.valueOf(empId1));
                traineeDto.setName(String.valueOf(name));
                traineeDto.setEmailId(String.valueOf(email));
                traineeDto.setPassword(passwordEncoder.encode(String.valueOf(password)));
                traineeDto.setRole(Role.ROLE_TRAINEE);
                traineeDto.setBatchId(batchId);
//                traineeDto.setBatchDto(batchDto);
                traineeDtos.add(traineeDto);
            }
            if (!traineeDtos.isEmpty()) {
                uploadFile(traineeDtos, batchId);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Unexcepted Error";
    }

    public String processExcelTrainee(MultipartFile file, int batchId) {
        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook;
            String fileName = file.getOriginalFilename();
            if (fileName != null && fileName.toLowerCase().endsWith(".xls")) {
                workbook = new HSSFWorkbook(inputStream); // For .xls files
            } else if (fileName != null && fileName.toLowerCase().endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(inputStream); // For .xlsx files
            } else {
                return "Unsupported file format. Please upload the correct file format.";
            }
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);
            Map<String, Integer> columnIndexMap = new HashMap<>();

            String[] expectedHeaders = {"User_ID", "Employee Id", "Name","Email_Official","Contact Number"};

            for (Cell cell : headerRow) {
                String columnHeader = cell.getStringCellValue().trim();
                columnIndexMap.put(columnHeader, cell.getColumnIndex());
            }

            for (String expectedHeader : expectedHeaders) {
                if (!columnIndexMap.containsKey(expectedHeader)) {
                    return "Missing header: " + expectedHeader;
                }
            }

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            List<TraineeDto> traineeDtos = new ArrayList<>();
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    TraineeDto traineeDto = new TraineeDto();
                    traineeDto.setTraineeDetail(new TraineeDetailDto());
                    for (Map.Entry<String, Integer> entry : columnIndexMap.entrySet()) {
                        String columnName = entry.getKey();
                        int columnIndex = entry.getValue();
                        Cell cell = row.getCell(columnIndex);
                        if (cell != null) {
                            switch (columnName) {
                                case "Employee Id":
                                    int empId = (int) cell.getNumericCellValue();
                                    traineeDto.setEmployeeId(String.valueOf(empId));
                                    break;
                                case "Name":
                                    traineeDto.setName(cell.getStringCellValue());
                                    break;
                                case "Email_Official":
                                    traineeDto.setEmailId(cell.getStringCellValue());
                                    break;
                                case "DOJ":
                                    try {
                                        if (DateUtil.isCellDateFormatted(cell)) {
                                            Date date = cell.getDateCellValue();
                                            traineeDto.getTraineeDetail().setDoj(date);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case "Grade":
                                    int grade = (int) cell.getNumericCellValue();
                                    traineeDto.getTraineeDetail().setGrade(grade);
                                    break;
                                case "IBU":
                                    traineeDto.getTraineeDetail().setIbu(cell.getStringCellValue());
                                    break;
                                case "Function":
                                    traineeDto.getTraineeDetail().setFunction(cell.getStringCellValue());
                                    break;
                                case "IO":
                                    traineeDto.getTraineeDetail().setIo(cell.getStringCellValue());
                                    break;
                                case "Designation":
                                    traineeDto.getTraineeDetail().setDesignation(cell.getStringCellValue());
                                    break;
                                case "Tier Categorization":
                                    int tierCategorization = (int) cell.getNumericCellValue();
                                    traineeDto.getTraineeDetail().setTierCategorization(tierCategorization);
                                    break;
                                case "Probation Period (Months)":
                                    int probationPeriod = (int) cell.getNumericCellValue();
                                    traineeDto.getTraineeDetail().setProbationPeriod(probationPeriod);
                                    break;
                                case "Personal Email Id":
                                    traineeDto.getTraineeDetail().setPersonalEmailId(cell.getStringCellValue());
                                    break;
                                case "Contact Number":
                                    traineeDto.getTraineeDetail().setContactNumber("" + (long)cell.getNumericCellValue());
                                    break;
                                case "Date of birth":
                                    try {
                                        if (DateUtil.isCellDateFormatted(cell)) {
                                            Date date = cell.getDateCellValue();
                                            traineeDto.getTraineeDetail().setDob(date);
                                        } else if (cell.getCellType() == CellType.STRING) {
                                            String dateStr = cell.getStringCellValue();
                                            Date date = formatter.parse(dateStr);
                                            traineeDto.getTraineeDetail().setDoj(date);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case "10%":
                                    double tenthPercent = cell.getNumericCellValue();
                                    traineeDto.getTraineeDetail().setTenthPercent(tenthPercent);
                                    break;
                                case "12%":
                                    double twelfthPercent = cell.getNumericCellValue();
                                    traineeDto.getTraineeDetail().setTwelfthPercent(twelfthPercent);
                                    break;
                                case "Graduation":
                                    traineeDto.getTraineeDetail().setGraduation(cell.getStringCellValue());
                                    break;
                                case "Graduation %":
                                    double graduationPercent = cell.getNumericCellValue();
                                    traineeDto.getTraineeDetail().setGraduationPercent(graduationPercent);
                                    break;
                                case "Branch":
                                    traineeDto.getTraineeDetail().setBranch(cell.getStringCellValue());
                                    break;
                                case "Graduation YOP":
                                    int graduationYOP = (int) cell.getNumericCellValue();
                                    traineeDto.getTraineeDetail().setGraduationYOP(graduationYOP);
                                    break;
                                case "College Name (graduation)":
                                    traineeDto.getTraineeDetail().setCollegeName(cell.getStringCellValue());
                                    break;
                                case "University":
                                    traineeDto.getTraineeDetail().setUniversity(cell.getStringCellValue());
                                    break;
                                case "uni_short name":
                                    traineeDto.getTraineeDetail().setUniversityShortName(cell.getStringCellValue());
                                    break;
                                case "Address*":
                                    traineeDto.getTraineeDetail().setAddress(cell.getStringCellValue());
                                    break;
                                case "City*":
                                    traineeDto.getTraineeDetail().setCity(cell.getStringCellValue());
                                    break;
                                case "State*":
                                    traineeDto.getTraineeDetail().setState(cell.getStringCellValue());
                                    break;
                                case "Country*":
                                    traineeDto.getTraineeDetail().setCountry(cell.getStringCellValue());
                                    break;
                                case "PIN Code":
                                    int pinCode = (int) cell.getNumericCellValue();
                                    traineeDto.getTraineeDetail().setPinCode(pinCode);
                                    break;
                                case "User_ID":
                                    traineeDto.getTraineeDetail().setUserId(cell.getStringCellValue());
                                    break;
                                case "Confirmation Date":
                                    try {
                                        if (DateUtil.isCellDateFormatted(cell)) {
                                            Date date = cell.getDateCellValue();
                                            traineeDto.getTraineeDetail().setConfirmationDate(date);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    traineeDto.setRole(Role.ROLE_TRAINEE);
                    traineeDto.setBatchId(batchId);
                    traineeDto.getTraineeDetail().setEmployeeId(traineeDto.getEmployeeId());
                    String generate = generatePassword(traineeDto.getName(), traineeDto.getEmployeeId());
                    traineeDto.setPassword(passwordEncoder.encode(generate));
                    traineeDtos.add(traineeDto);
                }
            }
            if (!traineeDtos.isEmpty()) {
                uploadFile(traineeDtos, batchId);
                return "Successfully";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Unexpected error while processing the file.";
    }

    private String generatePassword(String name, String employeeId) {
        name = name.split(" ")[0];
        String password = (name + employeeId + "#");
        return password.replace("a", "@");
    }

    @Override
    public boolean addQuestion(QuestionDto questionDto, int labId) {
        LabDto labDto = labRepo.getLab(labId);
        if (labDto != null) {
            List<QuestionDto> questionList = labDto.getQuestions();
            questionList.add(questionDto);
            labDto.setQuestions(questionList);
            labRepo.updateLab(labDto);
        }
        return true;
    }

    @Override
    public boolean addQuestionInLabTest(QuestionDto questionDto, int labId) {
        return true;
    }

    @Override
    public String addQuestionInLabTestViaExcel(MultipartFile file, int labTestId) {
        String s = processExcelForLabTest(file, labTestId);
        return s;
    }

    @Override
    public String addQuestion(MultipartFile file, int labId) {
        String s= processExcelForLab(file, labId);
        return s;
    }

    @Override
    public void registerAdmin(AdminDto admin) throws UserAlreadyExist {
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setLabs(new ArrayList<>());
        admin.setLabTests(new ArrayList<>());
        admin.setBatches(new ArrayList<>());
        adminRepo.saveAdmin(admin);
    }

    @Override
    public boolean editQuestion(QuestionDto questionDto, int labId, int questionId) {
//        labRepo.updateQuestion(questionDto);
        labRepo.updateQuestionByHql(questionDto);
        return true;
    }

    @Override
    public QuestionDto getQuestion(int labId, int questionId) {
        QuestionDto questionDto = labRepo.getQuestion(labId, questionId);
        return questionDto;
    }

    @Override
    public boolean deleteQuestion(int labId, int questionId) {
        boolean remove = labRepo.removeQuestionFromJoinTable(labId, questionId);
        if (remove)
            labRepo.removeQuestionFromTableById(questionId);
        return true;
    }

    public String processExcelForLab(MultipartFile file, int labId) {
        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook;
            String fileName = file.getOriginalFilename();
            if (fileName != null && fileName.toLowerCase().endsWith(".xls")) {
                workbook = new HSSFWorkbook(inputStream); // For .xls files
            } else if (fileName != null && fileName.toLowerCase().endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(inputStream); // For .xlsx files
            } else {
                return "Unsupported file format. Please upload correct file format.";
            }
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);
            Map<String, Integer> columnIndexMap = new HashMap<>();

            String[] expectedHeaders = {"Question Description", "Question Answer", "Question Points"};

            for (Cell cell : headerRow) {
                String columnHeader = cell.getStringCellValue().trim();
                for (String expectedHeader : expectedHeaders) {
                    if (columnHeader.equalsIgnoreCase(expectedHeader)) {
                        columnIndexMap.put(columnHeader, cell.getColumnIndex());
                        break;
                    }
                }
            }

            for (String expectedHeader : expectedHeaders) {
                if (!columnIndexMap.containsKey(expectedHeader)) {
                    return "Missing header: " + expectedHeader;
                }
            }

            List<QuestionDto> questionDtos = new ArrayList<>();
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Cell questionCell = row.getCell(0);
                    Cell actualQueryCell = row.getCell(1);
                    Cell questionPointsCell = row.getCell(2);
                    QuestionDto questionDto = new QuestionDto();
                    if (questionCell != null) {
                        questionDto.setQuestionDescription(String.valueOf(questionCell));
                    }
                    if (actualQueryCell != null) {
                        questionDto.setQuestionAnswer(String.valueOf(actualQueryCell));
                    }
                    if (questionPointsCell != null) {
                        if (questionPointsCell.getCellType() == CellType.NUMERIC && questionPointsCell.getNumericCellValue() > 0) {
                            double points = questionPointsCell.getNumericCellValue();
                            questionDto.setQuestionPoints((int) points);
                        } else {
                            int questionPoint = labRepo.getQuestionPoint(labId);
                            questionDto.setQuestionPoints(questionPoint);
                        }
                    }
                    questionDtos.add(questionDto);
                }
            }
            if (!questionDtos.isEmpty()) {
                LabDto labDto = labRepo.getLab(labId);
                List<QuestionDto> questionList = labDto.getQuestions();
                questionList.addAll(questionDtos);
                labDto.setQuestions(questionList);
                labRepo.updateLab(labDto);
                return "Successfully";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Unexpected error while processing the file.";
    }

    public String processExcelForLabTest(MultipartFile file, int labTestId) {
        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook;
            String fileName = file.getOriginalFilename();
            if (fileName != null && fileName.toLowerCase().endsWith(".xls")) {
                workbook = new HSSFWorkbook(inputStream); // For .xls files
            } else if (fileName != null && fileName.toLowerCase().endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(inputStream); // For .xlsx files
            } else {
                return "Unsupported file format. Please upload correct file format.";
            }
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);
            Map<String, Integer> columnIndexMap = new HashMap<>();

            String[] expectedHeaders = {"Question Description", "Question Answer", "Question Points"};

            for (Cell cell : headerRow) {
                String columnHeader = cell.getStringCellValue().trim();
                for (String expectedHeader : expectedHeaders) {
                    if (columnHeader.equalsIgnoreCase(expectedHeader)) {
                        columnIndexMap.put(columnHeader, cell.getColumnIndex());
                        break;
                    }
                }
            }

            for (String expectedHeader : expectedHeaders) {
                if (!columnIndexMap.containsKey(expectedHeader)) {
                    return "Missing header: " + expectedHeader;
                }
            }

            List<QuestionDto> questionDtos = new ArrayList<>();
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Cell questionCell = row.getCell(0);
                    Cell actualQueryCell = row.getCell(1);
                    Cell questionPointsCell = row.getCell(2);
                    QuestionDto questionDto = new QuestionDto();
                    if (questionCell != null) {
                        questionDto.setQuestionDescription(String.valueOf(questionCell));
                    }
                    if (actualQueryCell != null) {
                        questionDto.setQuestionAnswer(String.valueOf(actualQueryCell));
                    }
                    if (questionPointsCell != null) {
                        if (questionPointsCell.getCellType() == CellType.NUMERIC && questionPointsCell.getNumericCellValue() > 0) {
                            double points = questionPointsCell.getNumericCellValue();
                            questionDto.setQuestionPoints((int) points);
                        } else {
                            int questionPoint = labTestRepo.getQuestionPoint(labTestId);
                            questionDto.setQuestionPoints(questionPoint);
                        }
                    }
                    questionDtos.add(questionDto);
                }
            }
            if (!questionDtos.isEmpty()) {
                LabTestDto labTestDto = labTestRepo.getLabTest(labTestId);
                List<QuestionDto> questionList = labTestDto.getQuestions();
                questionList.addAll(questionDtos);
                labTestDto.setQuestions(questionList);
                labTestRepo.updateLabTest(labTestDto);
                return "Successfully";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Unexpected error while processing the file.";
    }

    @Override
    public boolean uploadFile(List<TraineeDto> traineeDtos, int batchId) {
        BatchDto batchDto = batchRepo.getBatchById(batchId);
        List<TraineeDto> traineeList = batchDto.getTrainees();
        traineeList.addAll(traineeDtos);
        batchDto.setTrainees(traineeList);
        batchRepo.updateBatch(batchDto);
        return false;
    }

    @Override
    public TraineeDto registerTrainee(TraineeDto traineeDto, int batchId) {
        String password = traineeDto.getPassword();
        traineeDto.setPassword(passwordEncoder.encode(password));
        traineeDto.setRole(Role.ROLE_TRAINEE);
        traineeDto.setBatchId(batchId);
        TraineeDto traineeDto1 = new TraineeDto();
        traineeDto1.setBatchId(batchId);
        traineeDto1.setName(traineeDto.getName());
        traineeDto1.setPassword(traineeDto.getPassword());
        traineeDto1.setEmailId(traineeDto.getEmailId());
        traineeDto1.setRole(traineeDto.getRole());
        traineeDto1.setEmployeeId(traineeDto.getEmployeeId());
        List<TraineeDto> traineeDtos = new ArrayList<>();
        traineeDtos.add(traineeDto1);
        uploadFile(traineeDtos, batchId);
        return traineeDto1;
    }


}
