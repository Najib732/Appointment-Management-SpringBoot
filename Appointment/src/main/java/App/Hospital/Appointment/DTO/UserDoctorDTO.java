package App.Hospital.Appointment.DTO;

import lombok.Data;

@Data
public class UserDoctorDTO {
    Integer id;
    String name;
    String specialization;
    Boolean isAvailable;
}
