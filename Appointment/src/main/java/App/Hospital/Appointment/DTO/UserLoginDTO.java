package App.Hospital.Appointment.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserLoginDTO {
    @NotNull(message = "Email is required")
    @Email(message = "Insert an Email")
    String Email;
    @NotNull(message = "password is required")
    String password;



}
