package app.dto.user;

import app.validator.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

@Data
@Accessors(chain = true)
public class UserRegistrationRequestDto {
    @Email
    private String email;
    @Length(min = 6, max = 20)
    private String password;
    @Length(min = 6, max = 20)
    private String repeatPassword;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
}
