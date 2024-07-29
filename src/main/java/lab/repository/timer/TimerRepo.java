package lab.repository.timer;

import lab.entity.timer.Timer;

public interface TimerRepo {
    Timer getTestTimer(int batchId, int testId, String traineeId);

    int postTestTimer(Timer timer);

    Timer updateTestTimer(Timer timer);
}
