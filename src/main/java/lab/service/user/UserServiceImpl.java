package lab.service.user;

import lab.dto.user.UserDto;
import lab.repository.user.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;
    @Override
    public UserDto getUserById(String id) {
        return userRepo.getUserByEmpId(id);
    }

    @Override
    public List<UserDto> getUsers() {
        return null;
    }

    @Override
    public String getUserNameById(String userName) {
        return userRepo.getUserByEmpId(userName).getName();
    }
}
