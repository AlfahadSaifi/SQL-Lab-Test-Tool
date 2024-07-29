package lab.service.user;
import lab.dto.user.UserDto;
import java.util.List;
public interface UserService {
    UserDto getUserById(String id);
    List<UserDto> getUsers();

    String getUserNameById(String userName);
}
