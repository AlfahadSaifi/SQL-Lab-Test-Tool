package lab.service.timer;

import lab.entity.timer.Timer;
import lab.repository.timer.TimerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional
public class TimerServiceImpl implements TimerService {
    @Autowired
    private TimerRepo timerRepo;
    @Override
    public Timer getTestTimer(int batchId, int testId, String traineeId) {
        Timer timer=  timerRepo.getTestTimer(batchId, testId, traineeId);
        return timer;
    }

    @Override
    public int postTestTimer(Timer timer) {
        return timerRepo.postTestTimer(timer);
    }

    @Override
    public Timer updateTestTimer(Timer timer) {
//        Timer timer = getTestTimer(timer);
        return timerRepo.updateTestTimer(timer);
    }
}
