package lab.service.user;

import lab.dto.user.UserDto;
import lab.repository.user.UserRepo;
import lab.security.UserDetailsServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
//public class UserDetailsServiceCustomImpl implements UserDetailsService {
public class UserDetailsServiceCustomImpl implements UserDetailsServiceInterface {
    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String emplId) throws UsernameNotFoundException {
        UserDto userDto = userRepo.getUserByEmpId(emplId);
        if (userDto == null) {
            throw new UsernameNotFoundException("User Not Found ");
        }
        return new UserDetailsCustomImpl(userDto);
    }
}
