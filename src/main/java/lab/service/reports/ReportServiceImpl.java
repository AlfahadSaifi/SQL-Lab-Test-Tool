package lab.service.reports;

import lab.dto.evaluation.*;
import lab.dto.lab.LabDto;
import lab.dto.lab.LabTestDto;
import lab.dto.trainee.TraineeDto;
import lab.entity.evaluation.Status;
import lab.repository.lab.LabRepo;
import lab.repository.labtest.LabTestRepo;
import lab.repository.reports.ReportRepo;
import lab.repository.trainee.TraineeRepo;
import lab.service.lab.LabService;
import lab.service.labtest.LabTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportRepo reportRepo;

    @Autowired
    private LabRepo labRepo;
    @Autowired
    private LabTestRepo labTestRepo;

    @Autowired
    private LabService labService;
    @Autowired
    private LabTestService labTestService;

    @Autowired
    private TraineeRepo traineeRepo;

    // for Lab
    public void saveRecordAttempt(RecordLabAttemptDto recordLabAttemptDto, String userQuery, boolean isCorrect) {
        LabSubmitQueryDto submitQuery = new LabSubmitQueryDto();
        submitQuery.setQuerySubmit(userQuery);
        if (isCorrect) {
            submitQuery.setStatus(Status.CORRECT);
        } else {
            submitQuery.setStatus(Status.INCORRECT);
        }
        RecordLabAttemptDto recordLabAttemptDto2 = reportRepo.fetchRecordAttempt(recordLabAttemptDto.getTraineeId(),
                recordLabAttemptDto.getLabId(),
                recordLabAttemptDto.getQuestionId());
        if (recordLabAttemptDto2 != null) {
            List<LabSubmitQueryDto> submitQueryDtos = recordLabAttemptDto2.getLabSubmitQueries();
            if (submitQueryDtos == null) {
                submitQueryDtos = new ArrayList<>();
            }
            submitQueryDtos.add(submitQuery);
            recordLabAttemptDto.setLabSubmitQueries(submitQueryDtos);
        }
        reportRepo.saveRecordAttempt(recordLabAttemptDto);
    }

    @Override
    public RecordLabAttemptDto fetchRecordAttempt(String userName, int labId, int questionId) {
        return reportRepo.fetchRecordAttempt(userName, labId, questionId);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void submitLab(int labId1, String userName) {
        TraineeDto traineeDto = traineeRepo.getTraineeByUserName(userName);
        int batchId = traineeDto.getBatchId();
        LabDto labDto = labService.getLabById(labId1);
        List<LabReportsDto> reportsDtoList = labDto.getReports();
        if (reportsDtoList.isEmpty()) {
            LabReportsDto reportsDto = new LabReportsDto();
            reportsDto.setBatchId(batchId);
            List<TraineeLabReportsDto> traineeLabReportsDtoList = new ArrayList<>();
            TraineeLabReportsDto traineeLabReportsDto = new TraineeLabReportsDto();
            traineeLabReportsDto.setTrainee(traineeDto);
            List<RecordLabAttemptDto> recordLabAttemptDtoList = reportRepo.fetchRecordAttempt(userName, labId1);
            traineeLabReportsDto.setRecordAttempt(recordLabAttemptDtoList);
            traineeLabReportsDtoList.add(traineeLabReportsDto);
            reportsDto.setTraineeLabReports(traineeLabReportsDtoList);
            reportsDtoList.add(reportsDto);
            labDto.setReports(reportsDtoList);
        } else {
            LabReportsDto reportsDto = null;
            for (LabReportsDto report : reportsDtoList) {
                if (report.getBatchId() == batchId) {
                    reportsDto = report;
                    break;
                }
            }
            if (reportsDto == null) {
                LabReportsDto reportsDto1 = new LabReportsDto();
                reportsDto1.setBatchId(batchId);
                List<TraineeLabReportsDto> traineeLabReportsDtoList = new ArrayList<>();
                TraineeLabReportsDto traineeLabReportsDto = new TraineeLabReportsDto();
                traineeLabReportsDto.setTrainee(traineeDto);
                List<RecordLabAttemptDto> recordLabAttemptDtoList = reportRepo.fetchRecordAttempt(userName, labId1);
                traineeLabReportsDto.setRecordAttempt(recordLabAttemptDtoList);
                traineeLabReportsDtoList.add(traineeLabReportsDto);
                reportsDto1.setTraineeLabReports(traineeLabReportsDtoList);
                reportsDtoList.add(reportsDto1);
            } else {
                List<TraineeLabReportsDto> traineeLabReportsDtosList = reportsDto.getTraineeLabReports();
                TraineeLabReportsDto traineeLabReportsDto = null;
                for (TraineeLabReportsDto e : traineeLabReportsDtosList) {
                    if (Objects.equals(e.getTrainee().getId(), traineeDto.getId())) {
                        traineeLabReportsDto = e;
                        break;
                    }
                }
                if (traineeLabReportsDto == null) {
                    reportsDtoList.remove(reportsDto);
                    traineeLabReportsDto = new TraineeLabReportsDto();
                    traineeLabReportsDto.setTrainee(traineeDto);
                    List<RecordLabAttemptDto> recordLabAttemptDtoList = reportRepo.fetchRecordAttempt(userName, labId1);
                    traineeLabReportsDto.setRecordAttempt(recordLabAttemptDtoList);
                } else {
                    reportsDtoList.remove(reportsDto);
                    List<RecordLabAttemptDto> recordLabAttemptDtosList = reportRepo.fetchRecordAttempt(userName, labId1);
                    traineeLabReportsDtosList.remove(traineeLabReportsDto);
                    traineeLabReportsDto.setRecordAttempt(recordLabAttemptDtosList);
                }
                traineeLabReportsDtosList.add(traineeLabReportsDto);
                reportsDto.setTraineeLabReports(traineeLabReportsDtosList);
                reportsDtoList.add(reportsDto);
            }
            labDto.setReports(reportsDtoList);
        }
        labRepo.updateLab(labDto);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void submitLab1(int labId1, String userName) {
        TraineeDto traineeDto = traineeRepo.getTraineeByUserName(userName);
        int batchId = traineeDto.getBatchId();

        // fetch the old Report of this lab
        LabDto labDto = labRepo.getLab(labId1);
        List<LabReportsDto> reportsDtoList = labDto.getReports();
        if (reportsDtoList.isEmpty()) {   //   if no record found
            LabReportsDto reportsDto = new LabReportsDto();
            reportsDto.setBatchId(batchId);
            List<TraineeLabReportsDto> traineeLabReportsDtos = getTraineeLabReports(batchId);
            TraineeLabReportsDto traineeLabReportsDto = new TraineeLabReportsDto();
            traineeLabReportsDto.setTrainee(traineeDto);
            List<RecordLabAttemptDto> recordLabAttemptDto = reportRepo.fetchRecordAttempt(userName, labId1);
            traineeLabReportsDto.setRecordAttempt(recordLabAttemptDto);
            traineeLabReportsDtos.add(traineeLabReportsDto);
            reportsDto.setTraineeLabReports(traineeLabReportsDtos);
            reportsDtoList.add(reportsDto);
        } else {  // if lab reports founds
            LabReportsDto reportsDto = labRepo.getLabReportByBatchId(batchId);
            List<TraineeLabReportsDto> traineeLabReportsDtoList = reportsDto.getTraineeLabReports();
            TraineeLabReportsDto traineeLabReportsDto = null;
            for (TraineeLabReportsDto e : traineeLabReportsDtoList) {
                if (Objects.equals(e.getTrainee().getId(), traineeDto.getId())) {
                    traineeLabReportsDto = e;
                    break;
                }
            }
            if (traineeLabReportsDto == null) {
                traineeLabReportsDto = new TraineeLabReportsDto();
                traineeLabReportsDto.setTrainee(traineeDto);
                List<RecordLabAttemptDto> recordLabAttemptDto = reportRepo.fetchRecordAttempt(userName, labId1);
                traineeLabReportsDto.setRecordAttempt(recordLabAttemptDto);
                traineeLabReportsDtoList.add(traineeLabReportsDto);
            } else {
                List<RecordLabAttemptDto> recordLabAttemptDto = reportRepo.fetchRecordAttempt(userName, labId1);
                traineeLabReportsDto.setRecordAttempt(recordLabAttemptDto);
            }
        }
        labRepo.submitLabReport(labDto);
    }

    private List<TraineeLabReportsDto> getTraineeLabReports(int batchId) {
        LabReportsDto reportsDto = labRepo.getLabReportByBatchId(batchId);
        List<TraineeLabReportsDto> traineeLabReportsDto = reportsDto.getTraineeLabReports();
        if (traineeLabReportsDto == null) {
            traineeLabReportsDto = new ArrayList<>();
        }
        return traineeLabReportsDto;
    }

    public void submitLab3(int labId1, String userName) {
        TraineeDto traineeDto = traineeRepo.getTraineeByUserName(userName);
        int batchId = traineeDto.getBatchId();
//        fetch the old Report of this lab
        LabDto labDto = labRepo.getLab(labId1);
        List<LabReportsDto> reportsDtoList = labDto.getReports();


//        is any Lab Report exist or not
        if (reportsDtoList == null) {
//            create a new Report list
            reportsDtoList = new ArrayList<>();
//            add new Report in that list
            LabReportsDto reportsDto = new LabReportsDto();
            reportsDto.setBatchId(batchId);
            TraineeLabReportsDto traineeLabReportsDto = new TraineeLabReportsDto();
            traineeLabReportsDto.setTrainee(traineeDto);
            List<RecordLabAttemptDto> recordLabAttemptDto = reportRepo.fetchRecordAttempt(userName, labId1);
            if (recordLabAttemptDto == null) {
                recordLabAttemptDto = new ArrayList<>();
                RecordLabAttemptDto recordLabAttemptDto1 = new RecordLabAttemptDto();
                recordLabAttemptDto.add(recordLabAttemptDto1);
            }
            traineeLabReportsDto.setRecordAttempt(recordLabAttemptDto);
            List<TraineeLabReportsDto> traineeLabReportsDtos = new ArrayList<>();
            traineeLabReportsDtos.add(traineeLabReportsDto);
            reportsDto.setTraineeLabReports(traineeLabReportsDtos);
            reportsDtoList.add(reportsDto);
        } else {
            LabReportsDto reportsDto = null;
            //find old report by batchId
            for (LabReportsDto reports : reportsDtoList) {
                if (reports.getBatchId() == batchId) {
                    reportsDto = reports;
                    break;
                }
            }

//        if  old report not found
            if (reportsDto == null) {
                reportsDto = new LabReportsDto();
                reportsDto.setBatchId(batchId);
                TraineeLabReportsDto traineeLabReportsDto = new TraineeLabReportsDto();
                traineeLabReportsDto.setTrainee(traineeDto);
                List<RecordLabAttemptDto> recordLabAttemptDto = reportRepo.fetchRecordAttempt(userName, labId1);
                if (recordLabAttemptDto == null) {
                    recordLabAttemptDto = new ArrayList<>();
                    RecordLabAttemptDto recordLabAttemptDto1 = new RecordLabAttemptDto();
                    recordLabAttemptDto.add(recordLabAttemptDto1);
                }
                traineeLabReportsDto.setRecordAttempt(recordLabAttemptDto);
                List<TraineeLabReportsDto> traineeLabReportsDtos = new ArrayList<>();
                traineeLabReportsDtos.add(traineeLabReportsDto);
                reportsDto.setTraineeLabReports(traineeLabReportsDtos);
                reportsDtoList.add(reportsDto);
            } else {
                List<TraineeLabReportsDto> traineeLabReportsDtoList = reportsDto.getTraineeLabReports();
                List<RecordLabAttemptDto> recordLabAttemptDto = reportRepo.fetchRecordAttempt(userName, labId1);
                TraineeLabReportsDto traineeLabReportsDto = null;
                for (TraineeLabReportsDto e : traineeLabReportsDtoList) {
                    String userName1 = e.getTrainee().getName();
                    if (traineeDto.getName().equals(userName1)) {
                        traineeLabReportsDto = e;
                        break;
                    }
                }
                if (traineeLabReportsDto == null) {
                    traineeLabReportsDto = new TraineeLabReportsDto();
                }
                traineeLabReportsDto.setRecordAttempt(recordLabAttemptDto);
                traineeLabReportsDtoList.add(traineeLabReportsDto);
                reportsDto.setTraineeLabReports(traineeLabReportsDtoList);
            }
            labDto.setReports(reportsDtoList);
            labRepo.submitLabReport(labDto);
        }
    }

    @Override
    public TraineeLabReportsDto getLabTraineeRecord(int reportId) {
        return reportRepo.fetchTraineeReports(reportId);
    }

    @Override
    public RecordLabTestAttemptDto fetchRecordLabTestAttempt(String traineeId, int labTestId, int questionId) {
        return reportRepo.fetchRecordLabTestAttempt(traineeId, labTestId, questionId);
    }

    @Override
    public void saveRecordLabTestAttempt(RecordLabTestAttemptDto recordLabTestAttemptDto, String query, boolean isCorrect) {
        LabTestSubmitQueryDto submitQuery = new LabTestSubmitQueryDto();
        submitQuery.setQuerySubmit(query);
        if (isCorrect) {
            submitQuery.setStatus(Status.CORRECT);
        } else {
            submitQuery.setStatus(Status.INCORRECT);
        }

        RecordLabTestAttemptDto recordLabTestAttemptDto2 = reportRepo.fetchRecordLabTestAttempt(recordLabTestAttemptDto.getTraineeId(),
                recordLabTestAttemptDto.getLabTestId(),
                recordLabTestAttemptDto.getQuestionId());
        if (recordLabTestAttemptDto2 != null) {
            List<LabTestSubmitQueryDto> submitQueryDtos = recordLabTestAttemptDto2.getLabTestSubmitQueries();
            if (submitQueryDtos == null) {
                submitQueryDtos = new ArrayList<>();
            }
            submitQueryDtos.add(submitQuery);
            recordLabTestAttemptDto.setLabTestSubmitQueries(submitQueryDtos);
        }
        reportRepo.saveRecordLabTestAttempt(recordLabTestAttemptDto);
    }

    @Override
    public void submitLabTest(int labTestId, String userId) {
        TraineeDto traineeDto = traineeRepo.getTraineeByUserName(userId);
        int batchId = traineeDto.getBatchId();
        LabTestDto labTestDto = labTestService.getLabTestById(labTestId);
        List<LabTestReportsDto> labTestReportsDtoList = labTestDto.getLabTestReports();
        if (labTestReportsDtoList.isEmpty()) {
            LabTestReportsDto labTestReportsDto = new LabTestReportsDto();
            labTestReportsDto.setBatchId(batchId);
            List<TraineeLabTestReportsDto> traineeLabTestReportsDtoList = new ArrayList<>();
            TraineeLabTestReportsDto traineeLabTestReportsDto = new TraineeLabTestReportsDto();
            traineeLabTestReportsDto.setTrainee(traineeDto);
            List<RecordLabTestAttemptDto> recordLabTestAttemptDtoList = reportRepo.fetchLabTestRecordAttempt(userId, labTestId);
            traineeLabTestReportsDto.setRecordLabTestAttempt(recordLabTestAttemptDtoList);
            traineeLabTestReportsDtoList.add(traineeLabTestReportsDto);
            labTestReportsDto.setTraineeLabTestReports(traineeLabTestReportsDtoList);
            labTestReportsDtoList.add(labTestReportsDto);
            labTestDto.setLabTestReports(labTestReportsDtoList);
        } else {
            LabTestReportsDto labTestReportsDto = null;
            for (LabTestReportsDto report : labTestReportsDtoList) {
                if (report.getBatchId() == batchId) {
                    labTestReportsDto = report;
                    break;
                }
            }
            if (labTestReportsDto == null) {
                LabTestReportsDto labTestReportsDto1 = new LabTestReportsDto();
                labTestReportsDto1.setBatchId(batchId);
                List<TraineeLabTestReportsDto> traineeLabTestReportsDtoList = new ArrayList<>();
                TraineeLabTestReportsDto traineeLabTestReportsDto = new TraineeLabTestReportsDto();
                traineeLabTestReportsDto.setTrainee(traineeDto);
                List<RecordLabTestAttemptDto> recordLabTestAttemptDtoList = reportRepo.fetchLabTestRecordAttempt(userId, labTestId);
                traineeLabTestReportsDto.setRecordLabTestAttempt(recordLabTestAttemptDtoList);
                traineeLabTestReportsDtoList.add(traineeLabTestReportsDto);
                labTestReportsDto1.setTraineeLabTestReports(traineeLabTestReportsDtoList);
                labTestReportsDtoList.add(labTestReportsDto1);
            } else {
                List<TraineeLabTestReportsDto> traineeLabTestReportsDtosList = labTestReportsDto.getTraineeLabTestReports();
                TraineeLabTestReportsDto traineeLabTestReportsDto = null;
                for (TraineeLabTestReportsDto e : traineeLabTestReportsDtosList) {
                    if (Objects.equals(e.getTrainee().getId(), traineeDto.getId())) {
                        traineeLabTestReportsDto = e;
                        break;
                    }
                }
                if (traineeLabTestReportsDto == null) {
                    labTestReportsDtoList.remove(labTestReportsDto);
                    traineeLabTestReportsDto = new TraineeLabTestReportsDto();
                    traineeLabTestReportsDto.setTrainee(traineeDto);
                    List<RecordLabTestAttemptDto> recordLabTestAttemptDtoList = reportRepo.fetchLabTestRecordAttempt(userId, labTestId);
                    traineeLabTestReportsDto.setRecordLabTestAttempt(recordLabTestAttemptDtoList);
                } else {
                    labTestReportsDtoList.remove(labTestReportsDto);
                    List<RecordLabTestAttemptDto> recordLabTestAttemptDtosList = reportRepo.fetchLabTestRecordAttempt(userId, labTestId);
                    traineeLabTestReportsDtosList.remove(traineeLabTestReportsDto);
                    traineeLabTestReportsDto.setRecordLabTestAttempt(recordLabTestAttemptDtosList);
                }
                traineeLabTestReportsDtosList.add(traineeLabTestReportsDto);
                labTestReportsDto.setTraineeLabTestReports(traineeLabTestReportsDtosList);
                labTestReportsDtoList.add(labTestReportsDto);
            }
            labTestDto.setLabTestReports(labTestReportsDtoList);
        }
        labTestRepo.updateLabTest(labTestDto);
    }

    @Override
    public int getLabTestCount(String userId) {
        return reportRepo.getLabTestCount(userId);
    }

    @Override
    public int getLabCount(String userId) {
        return reportRepo.getLabCount(userId);
    }

    @Override
    public int getBatchCount(String userId) {
        return reportRepo.getBatchCount(userId);
    }

    @Override
    public int getTraineeCount() {
        return reportRepo.getTraineeCount();
    }

    @Override
    public int getLabCorrectAns(int reportId) {
        List<RecordLabAttemptDto> recordLabAttemptDtos = reportRepo.fetchTraineeReports(reportId).getRecordAttempt();
        int count = 0;
        for (RecordLabAttemptDto e : recordLabAttemptDtos) {
            for (LabSubmitQueryDto q1 : e.getLabSubmitQueries()) {
                if (q1.getStatus().equals(Status.CORRECT)) {
                    count++;
                    break;
                }
            }
        }
        return count;
    }

    @Override
    public int getLabAttemptQuestion(int reportId) {
        List<RecordLabAttemptDto> recordLabAttemptDtos = reportRepo.fetchTraineeReports(reportId).getRecordAttempt();
        Set<Integer> attempQuestion = new HashSet<>();
        for (RecordLabAttemptDto e : recordLabAttemptDtos) {
            attempQuestion.add(e.getQuestionId());
        }
        return attempQuestion.size();
    }

    @Override
    public int getLabIncorrectAns(int reportId) {
        return getLabAttemptQuestion(reportId) - getLabCorrectAns(reportId);
    }

    @Override
    public int getLabSkipQuestion(int labId, int reportId) {
        return labService.getQuestionCount(labId) - getLabAttemptQuestion(reportId);
    }

    @Override
    public List<RecordLabAttemptDto> fetchAllReports(int reportId) {
        return reportRepo.fetchAllReports(reportId);
    }
}
