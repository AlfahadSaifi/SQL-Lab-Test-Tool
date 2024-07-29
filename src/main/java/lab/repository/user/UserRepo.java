package lab.repository.user;

import lab.dto.user.UserDto;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepo {
    UserDto getUserByEmpId(String id);

}
