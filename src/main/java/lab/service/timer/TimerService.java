package lab.service.timer;

import lab.entity.timer.Timer;

public interface TimerService {

    Timer getTestTimer(int batchId, int testId, String traineeId);

    int postTestTimer(Timer timer);

    Timer updateTestTimer(Timer timer);
}
