package lab.gobal;

import lab.entity.evaluation.RecordLabAttempt;
import lab.entity.evaluation.RecordLabTestAttempt;
import lab.entity.lab.QuestionStatus;
import lab.payload.answer.QuestionPointsPayLoad;
import lab.payload.report.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportPayload {

    public static List<DetailLabTestReport> convertToDetailReportPayload(List<QuestionPointsPayLoad> questionList, List<RecordLabTestAttempt> labAttempts) {
        List<DetailLabTestReport> customDetails = new ArrayList<>();
        for (QuestionPointsPayLoad question : questionList) {
            DetailLabTestReport customDetail = new DetailLabTestReport();
            customDetail.setQuestionId(question.getQuestionId());
            customDetail.setQuestionDescription(question.getQuestionDescription());
            customDetail.setQuestionPoints(question.getQuestionPoints());
            for (RecordLabTestAttempt attempt : labAttempts) {
                if (attempt.getQuestionId() == question.getQuestionId()) {
                    customDetail.setTraineeCurrentQuestionPoints(attempt.getTraineeCurrentQuestionPoints());
                    customDetail.setLabSubmitQueries(attempt.getLabTestSubmitQueries());
                    break;
                }
            }
            customDetails.add(customDetail);
        }
        return customDetails;
    }
    public static List<DetailLabReport> convertToLabDetailReportPayload(List<QuestionPointsPayLoad> questionList, List<RecordLabAttempt> labAttempts) {
        List<DetailLabReport> customDetails = new ArrayList<>();
        for (QuestionPointsPayLoad question : questionList) {
            DetailLabReport customDetail = new DetailLabReport();
            customDetail.setQuestionId(question.getQuestionId());
            customDetail.setQuestionDescription(question.getQuestionDescription());
            customDetail.setQuestionPoints(question.getQuestionPoints());
            for (RecordLabAttempt attempt : labAttempts) {
                if (attempt.getQuestionId() == question.getQuestionId()) {
                    customDetail.setTraineeCurrentQuestionPoints(attempt.getTraineeCurrentQuestionPoints());
                    customDetail.setLabSubmitQueries(attempt.getLabSubmitQueries());
                    break;
                }
            }
            customDetails.add(customDetail);
        }
        return customDetails;
    }
    public static LabTestReportByBatch generateLabTestReports(
            List<LabTestReport> labTestReports,
            List<QuestionPointsPayLoad> questionPointsPayloads,
            String batchName,
            double totalScore
    ) {
        Map<Integer, List<LabTestReport>> labTestReportMap = labTestReports.stream()
                .collect(Collectors.groupingBy(report -> report.getLabTestInfo().getLabTestId()));
        Map<Integer, Integer> questionPointsMap = questionPointsPayloads.stream()
                .collect(Collectors.toMap(QuestionPointsPayLoad::getQuestionId, QuestionPointsPayLoad::getQuestionPoints));
        LabTestReportByBatch labTestReportByBatch = new LabTestReportByBatch();
        for (Map.Entry<Integer, List<LabTestReport>> entry : labTestReportMap.entrySet()) {
            int labTestId = entry.getKey();
            List<LabTestReport> testReports = entry.getValue();
            labTestReportByBatch.setLabTestId(labTestId);
            labTestReportByBatch.setBatchCode(batchName);
            labTestReportByBatch.setTotalScore(totalScore);
            List<TestDetail> testDetails = new ArrayList<>();
            for (LabTestReport testReport : testReports) {
                double obtainedScore = 0;
                for (QuestionStatus questionStatus : testReport.getLabTestInfo().getQuestionStatusList()) {
                    if (questionStatus.getStatus() == lab.entity.lab.Status.CORRECT)
                        obtainedScore += questionPointsMap.getOrDefault(questionStatus.getQuestionId(), 0);
                }
                TestDetail testDetail = new TestDetail();
                testDetail.setTraineeId(testReport.getLabTestInfo().getTraineeId());
                testDetail.setTraineeName(testReport.getTraineeName());
                testDetail.setLabTestStatus(testReport.getLabTestInfo().getLabTestStatus());
                testDetail.setObtainedScore(obtainedScore);
                testDetails.add(testDetail);
            }
            labTestReportByBatch.setTestDetails(testDetails);
        }
        return labTestReportByBatch;
    }
    public static List<DetailReportPayload> convertToDetailLabReportPayload(List<QuestionPointsPayLoad> questionList, List<RecordLabAttempt> labAttempts) {
        List<DetailReportPayload> customDetails = new ArrayList<>();
        for (QuestionPointsPayLoad question : questionList) {
            DetailReportPayload customDetail = new DetailReportPayload();
            customDetail.setQuestionId(question.getQuestionId());
            customDetail.setQuestionDescription(question.getQuestionDescription());
            customDetail.setQuestionPoints(question.getQuestionPoints());
            for (RecordLabAttempt attempt : labAttempts) {
                if (attempt.getQuestionId() == question.getQuestionId()) {
                    customDetail.setTraineeCurrentQuestionPoints(attempt.getTraineeCurrentQuestionPoints());
                    customDetail.setLabSubmitQueries(attempt.getLabSubmitQueries());
                    break;
                }
            }
            customDetails.add(customDetail);
        }
        return customDetails;

    }
    public static LabReportByBatch generateLabReports(List<LabReport> labReports, List<QuestionPointsPayLoad> questionPointsPayloads, String batchName, double totalScore) {
        Map<Integer, List<LabReport>> labReportMap = labReports.stream()
                .collect(Collectors.groupingBy(report -> report.getLabInfo().getLabId()));
        Map<Integer, Integer> questionPointsMap = questionPointsPayloads.stream()
                .collect(Collectors.toMap(QuestionPointsPayLoad::getQuestionId, QuestionPointsPayLoad::getQuestionPoints));
        LabReportByBatch labReportByBatch = new LabReportByBatch();
        for (Map.Entry<Integer, List<LabReport>> entry : labReportMap.entrySet()) {
            int labTestId = entry.getKey();
            List<LabReport> testReports = entry.getValue();
            labReportByBatch.setLabId(labTestId);
            labReportByBatch.setBatchCode(batchName);
            labReportByBatch.setTotalScore(totalScore);
            List<LabDetails> testDetails = new ArrayList<>();
            for (LabReport testReport : testReports) {
                double obtainedScore = 0;
                for (QuestionStatus questionStatus : testReport.getLabInfo().getQuestionStatusList()) {
                    if (questionStatus.getStatus() == lab.entity.lab.Status.CORRECT)
                        obtainedScore += questionPointsMap.getOrDefault(questionStatus.getQuestionId(), 0);
                }
                LabDetails testDetail = new LabDetails();
                testDetail.setTraineeId(testReport.getLabInfo().getTraineeId());
                testDetail.setTraineeName(testReport.getTraineeName());
                testDetail.setLabStatus(testReport.getLabInfo().getLabStatus());
                testDetail.setObtainedScore(obtainedScore);
                testDetails.add(testDetail);
            }
            labReportByBatch.setLabDetails(testDetails);
        }
        return labReportByBatch;
    }
}
