package lab.dto.user;
import lab.entity.user.Role;
import lab.entity.user.UserStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Size;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class UserDto {
    private Long id;

    @NotEmpty(message = "Required")
    private String employeeId;

    @NotEmpty(message = "Required")
    private String name;

    @NotEmpty(message = "Required")
    @Size(min = 8,message = "Minimum 8 Character required")
    private String password;

    @NotEmpty(message = "Required")
    @Email(message = "Email Format Not Matched")
    private String emailId;

    private Role role;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;
}
