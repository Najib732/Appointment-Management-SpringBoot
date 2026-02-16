package App.Hospital.Appointment.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data

public class UserRegistrationDTO {
    @NotNull(message = "name is required")
String name;
    @NotNull(message = "email is required")
    @Email
String email;
    @NotNull(message = "password is required")
    @Size(min=6,max=20,message = "password must be 6-20 characters")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
            message = "Password must have uppercase, lowercase, number, and special character"
    )
String password;
    @NotNull(message = "Set the role")
    @Pattern(regexp = "PATIENT|ADMIN", message = "Role must be either PATIENT or ADMIN")
    private String role;

}
