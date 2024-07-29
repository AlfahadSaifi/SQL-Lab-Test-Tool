package lab.repository.timer;

import lab.entity.lab.LabInfo;
import lab.entity.timer.Timer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TimerRepoImpl implements TimerRepo {
    @Autowired
    private SessionFactory sessionFactory;
    @Override
    public Timer getTestTimer(int batchId, int testId, String traineeId) {
        String hql = "from Timer where batch=:batchId AND testId=:testId AND traineeId=:traineeId";
        Session session = sessionFactory.getCurrentSession();
        Query<Timer> query = session.createQuery(hql, Timer.class);
        query.setParameter("batchId", batchId);
        query.setParameter("testId", testId);
        query.setParameter("traineeId", traineeId);
        Timer timer=query.uniqueResult();
        return timer;
    }

    @Override
    public int postTestTimer(Timer timer) {
        Session session=sessionFactory.getCurrentSession();
        session.saveOrUpdate(timer);
        return timer.getTimerLeft();
    }

    @Override
    public Timer updateTestTimer(Timer timer) {
        Session session = sessionFactory.getCurrentSession();
        session.update(timer);
        return timer;
    }
}
