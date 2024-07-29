package lab.repository.user;
import lab.convert.Convert;
import lab.dto.user.UserDto;
import lab.entity.user.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepoImpl implements UserRepo{

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public UserDto getUserByEmpId(String id) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "from User where employeeId =:username";
        Query<User> query = session.createQuery(hql,User.class);
        query.setParameter("username",id);
        User user = query.uniqueResult();
        UserDto userDto = Convert.getModelMapper().toDto(user,UserDto.class);
        session.evict(user);
        return userDto;
    }
}
