package App.Hospital.Appointment.DTO;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class UserAppointmentDTO {
    private Integer appointmentId;
    private Integer patientId;
    private Integer doctorId;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String status;
}
