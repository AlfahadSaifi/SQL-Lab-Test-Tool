package lab.controller.timer;

import lab.dto.lab.LabDto;
import lab.entity.timer.Timer;
import lab.service.timer.TimerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/timer")
public class TimerController {
    @Autowired
    TimerService timerService;
    @RequestMapping(path = "/getTimer")
    public ResponseEntity<?> getTimer(@RequestParam("batchId") int batchId,@RequestParam("testId") int testId,@RequestParam("traineeId") String traineeId, Model model) {
        Timer timer = timerService.getTestTimer(batchId,testId,traineeId);
        return ResponseEntity.ok().body(timer);
    }
    @PostMapping(path = "/updateTimer")
    public ResponseEntity<?> updateTimer(@RequestParam("batchId") int batchId,@RequestParam("testId") int testId,@RequestParam("traineeId") String traineeId,@RequestParam("timeLeft") int timeLeft, Model model) {
        Timer timer=timerService.getTestTimer(batchId, testId, traineeId);
        timer.setTimerLeft(timeLeft);
        timer = timerService.updateTestTimer(timer);
        return ResponseEntity.ok().body(timer);
    }
    @RequestMapping(path = "/postTimer")
    public ResponseEntity<?> postTimer(@RequestParam("batchId") int batchId,@RequestParam("testId") int testId,@RequestParam("traineeId") String traineeId,@RequestParam("timeLeft") int timeLeft, Model model) {
        Timer timer=new Timer();
        timer.setTimerLeft(timeLeft*60);
        timer.setBatch(batchId);
        timer.setTraineeId(traineeId);
        timer.setTestId(testId);
        timerService.postTestTimer(timer);
        return ResponseEntity.ok().body(timeLeft);
    }
}
