package App.Hospital.Appointment.Service;


import App.Hospital.Appointment.DTO.UserAppointmentDTO;
import App.Hospital.Appointment.DTO.UserDTO;
import App.Hospital.Appointment.Repository.UserAppointmentRepo;
import App.Hospital.Appointment.Repository.UserDoctorRepo;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAppointmentService {

    @Autowired
    UserAppointmentRepo userAppointmentRepo;

    @Autowired
    UserService userService;

    @Autowired
    UserEmailService userEmailService;

    @Autowired
    UserDoctorRepo userDoctorRepo;


    public boolean createAppointmentServiceFunction(UserAppointmentDTO userAppointmentDTO) {

        UserDTO currentUser = userService.ProfileInfoServiceFunction();
        userAppointmentDTO.setPatientId(currentUser.getId());

        if (!userDoctorRepo.doctorAvailable(userAppointmentDTO.getDoctorId())) {
            throw new IllegalStateException("Doctor is not available");
        }

        boolean alreadyBooked = userAppointmentRepo.isAppointmentBooked(
                userAppointmentDTO.getPatientId(),
                userAppointmentDTO.getDoctorId(),
                userAppointmentDTO.getAppointmentDate(),
                userAppointmentDTO.getAppointmentTime()
        );

        if (alreadyBooked) {
            throw new IllegalStateException("Appointment slot is already taken");
        }

        UserAppointmentDTO createdAppointment = userAppointmentRepo.insertAppointmentAndReturnId(userAppointmentDTO);

        if (createdAppointment.getAppointmentId() == null) {
            throw new IllegalStateException("Failed to create appointment");
        }

        if (userAppointmentRepo.createAppointment(userAppointmentDTO) == null) {
            throw new IllegalStateException("Failed to finalize appointment creation");
        }

        String body = String.format(
                "Your appointment is confirmed!\nID: %s\nDate: %s\nTime: %s",
                createdAppointment.getAppointmentId(),
                createdAppointment.getAppointmentDate(),
                createdAppointment.getAppointmentTime()
        );

        userEmailService.sendSimpleEmail(
                currentUser.getEmail(),
                "Appointment Confirmation",
                body
        );

        return true;
    }





    public boolean updateAppointmentServiceFunction(UserAppointmentDTO userAppointmentDTO) {

        if (userAppointmentDTO.getAppointmentId() == null) {
            throw new IllegalArgumentException("Appointment ID must not be null");
        }

        UserAppointmentDTO existing =
                userAppointmentRepo.getAppointmentByAppointmentId(
                        userAppointmentDTO.getAppointmentId()
                );

        if (existing == null) {
            throw new IllegalStateException("Appointment not found for ID: " + userAppointmentDTO.getAppointmentId());
        }

        UserDTO user = userService.ProfileInfoServiceFunction();
        userAppointmentDTO.setPatientId(user.getId());

        if (userAppointmentDTO.getDoctorId() == null) {
            userAppointmentDTO.setDoctorId(existing.getDoctorId());
        }

        if (userAppointmentDTO.getAppointmentDate() == null) {
            userAppointmentDTO.setAppointmentDate(existing.getAppointmentDate());
        }

        if (userAppointmentDTO.getAppointmentTime() == null) {
            userAppointmentDTO.setAppointmentTime(existing.getAppointmentTime());
        }

        boolean sameDate = existing.getAppointmentDate().equals(userAppointmentDTO.getAppointmentDate());
        boolean sameTime = existing.getAppointmentTime().equals(userAppointmentDTO.getAppointmentTime());

        if (sameDate && sameTime) {
            throw new IllegalStateException("No changes detected in appointment date or time");
        }

        boolean updated = userAppointmentRepo.updateAppointment(userAppointmentDTO);

        if (!updated) {
            throw new RuntimeException("Failed to update appointment with ID: " + userAppointmentDTO.getAppointmentId());
        }
        return  true;
    }



    public boolean deleteAppointmentServiceFunction(int appointmentId) {
        UserAppointmentDTO appointment = userAppointmentRepo.getAppointmentByAppointmentId(appointmentId);
        if (appointment == null) {
            throw new IllegalStateException("No appointment found with ID: " + appointmentId);
        }
        boolean deleted = userAppointmentRepo.deleteAppointment(appointmentId);
        if (!deleted) {
            throw new RuntimeException("Failed to delete appointment with ID: " + appointmentId);
        }

        System.out.println("Appointment with ID " + appointmentId + " deleted successfully.");
        return true;
    }


    public UserAppointmentDTO getAppointmentByIdServiceFunction(int id) {
        UserDTO currentUser = userService.ProfileInfoServiceFunction();
        UserAppointmentDTO appointment = userAppointmentRepo.getAppointmentById(id, currentUser.getId());
        if (appointment == null) {
            throw new IllegalStateException(
                    "No appointment found with ID: " + id + " for user ID: " + currentUser.getId()
            );
        }
        return appointment;
    }


    public List<UserAppointmentDTO> getAppointmentServiceFunction() {
        UserDTO currentUser = userService.ProfileInfoServiceFunction();
        List<UserAppointmentDTO> appointments = userAppointmentRepo.getAllAppointments(currentUser.getId());
        if (appointments == null || appointments.isEmpty()) {
            throw new IllegalStateException(
                    "No appointments found for user ID: " + currentUser.getId()
            );
        }
        return appointments;
    }






}
