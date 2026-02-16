package App.Hospital.Appointment.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserForgetPassDTO {

    @Email(message = "Email is Required")
    String email;

    @NotNull(message = "password is required")
    @Size(min=6,max=20,message = "password must be 6-20 characters")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
            message = "Password must have uppercase, lowercase, number, and special character"
    )
    String password;

}
