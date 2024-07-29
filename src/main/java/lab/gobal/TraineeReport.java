package lab.gobal;


import lab.payload.lab.LabPayload;
import lab.payload.labTest.LabTestPayload;
import lab.payload.report.LabTestPassPercentage;
import lab.payload.report.TraineeLabMarksPayload;
import lab.payload.report.TraineeMarksPayload;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TraineeReport {
    public static List<MergeLabPayload> mergeLabPayloads(List<LabPayload> labPayloads, List<TraineeLabMarksPayload> traineeLabMarksPayloads, String traineeId) {
        Map<Integer, LabPayload> labMap = new HashMap<>();
        Map<Integer, TraineeLabMarksPayload> traineeLabMap = new HashMap<>();
        List<MergeLabPayload> mergedLabPayloads = new ArrayList<>();

        // Put LabPayloads into labMap
        for (LabPayload labPayload : labPayloads) {
            labMap.put(labPayload.getLabId(), labPayload);
        }

        // Put TraineeLabMarksPayloads into traineeLabMap
        for (TraineeLabMarksPayload traineeLabMarksPayload : traineeLabMarksPayloads) {
            traineeLabMap.put(traineeLabMarksPayload.getLabId(), traineeLabMarksPayload);
        }

        // Merge arrays
        for (Integer labId : labMap.keySet()) {
            MergeLabPayload mergedLabPayload = new MergeLabPayload();
            mergedLabPayload.setLabId(labId);
            LabPayload labPayload = labMap.get(labId);
            mergedLabPayload.setLabName(labPayload.getLabName());
            mergedLabPayload.setNoOfQuestions(labPayload.getNoOfQuestions());
            mergedLabPayload.setTotalMarks((int) labPayload.getTotalMarks());

            if (traineeLabMap.containsKey(labId)) {
                TraineeLabMarksPayload traineeLabMarksPayload = traineeLabMap.get(labId);
                mergedLabPayload.setStatus("Completed");
                mergedLabPayload.setObtainedMarks(traineeLabMarksPayload.getObtainedMarks());
                mergedLabPayload.setTraineeId(traineeLabMarksPayload.getTraineeId());
            } else {
                mergedLabPayload.setStatus("Unattempted");
                mergedLabPayload.setObtainedMarks(0); // Set default value for unattempted
                mergedLabPayload.setTraineeId(traineeId); // Set default value for unattempted
            }
            mergedLabPayloads.add(mergedLabPayload);
        }

        return mergedLabPayloads;
    }
    public static List<LabTestByTraineePayload> mergeLabTestPayloads(
            List<LabTestPassPercentage> labTestPassPercentages,
            List<LabTestPayload> labTestPayloads,
            List<TraineeMarksPayload> traineeMarksPayloads,
            String traineeId) {
        Map<Integer, LabTestPassPercentage> labTestPassPercentageMap = new HashMap<>();
        Map<Integer, LabTestPayload> labTestPayloadMap = new HashMap<>();
        Map<Integer, TraineeMarksPayload> traineeMarksPayloadMap = new HashMap<>();
        List<LabTestByTraineePayload> finalLabTestPayloads = new ArrayList<>();
        // Fill labTestPassPercentageMap
        for (LabTestPassPercentage labTestPassPercentage : labTestPassPercentages) {
            labTestPassPercentageMap.put(labTestPassPercentage.getLabTestId(), labTestPassPercentage);
        }

        // Fill labTestPayloadMap
        for (LabTestPayload labTestPayload : labTestPayloads) {
            labTestPayloadMap.put(labTestPayload.getLabTestId(), labTestPayload);
        }

        // Fill traineeMarksPayloadMap
        for (TraineeMarksPayload traineeMarksPayload : traineeMarksPayloads) {
            traineeMarksPayloadMap.put(traineeMarksPayload.getLabTestId(), traineeMarksPayload);
        }

        // Merge data
        for (Integer labTestId : labTestPayloadMap.keySet()) {
            LabTestByTraineePayload finalPayload = new LabTestByTraineePayload();
            finalPayload.setLabTestId(labTestId);

            LabTestPayload labTestPayload = labTestPayloadMap.get(labTestId);
            finalPayload.setTotalLabTestPoints((int) labTestPayload.getTotalLabTestPoints());
            finalPayload.setLabTestName(labTestPayload.getLabTestName());

            if (labTestPassPercentageMap.containsKey(labTestId)) {
                LabTestPassPercentage labTestPassPercentage = labTestPassPercentageMap.get(labTestId);
                finalPayload.setPassPercentage(labTestPassPercentage.getPassPercentage());
                finalPayload.setLabTestStatus("Completed");
            } else {
                finalPayload.setPassPercentage(0.0); // Set default pass percentage
                finalPayload.setLabTestStatus("Unattempted");
            }

            if (traineeMarksPayloadMap.containsKey(labTestId)) {
                TraineeMarksPayload traineeMarksPayload = traineeMarksPayloadMap.get(labTestId);
                finalPayload.setObtainedMarks(traineeMarksPayload.getObtainedMarks());
                finalPayload.setTraineeId(traineeMarksPayload.getTraineeId());
            } else {
                finalPayload.setObtainedMarks(0.0); // Set default obtained marks
                finalPayload.setTraineeId(traineeId); // Set default traineeId
                finalPayload.setLabTestStatus("Unattempted");
            }

            finalLabTestPayloads.add(finalPayload);
        }

        return finalLabTestPayloads;
    }
}
