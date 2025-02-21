package app.dto.user;

import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserLoginRequestDto {
    @Email
    private String email;
    private String password;
}
