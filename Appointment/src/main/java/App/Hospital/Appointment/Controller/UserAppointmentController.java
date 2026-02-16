package App.Hospital.Appointment.Controller;


import App.Hospital.Appointment.DTO.UserAppointmentDTO;
import App.Hospital.Appointment.DTO.UserDoctorDTO;
import App.Hospital.Appointment.Service.UserAppointmentService;
import App.Hospital.Appointment.Service.UserDoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Retention;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@EnableMethodSecurity
@RequestMapping("User/Appointment")
public class UserAppointmentController {
    @Autowired
    UserDoctorService userDoctorService;
    @Autowired
    UserAppointmentService userAppointmentService;

    @GetMapping("/doctors")
    @PreAuthorize("hasRole('PATIENT')")
    public List<UserDoctorDTO> DoctorInfo(){
       return userDoctorService.getAllDoctors();
    }

    @PostMapping("/CreateAccount")
    @PreAuthorize("hasRole('PATIENT')")
    public String createAppointmentControllerFunction(
            @RequestParam int doctorId,
            @RequestParam LocalDate appointmentDate,
            @RequestParam LocalTime appointmentTime
    ) {
        UserAppointmentDTO dto = new UserAppointmentDTO();
        dto.setDoctorId(doctorId);
        dto.setAppointmentDate(appointmentDate);
        dto.setAppointmentTime(appointmentTime);

        boolean value = userAppointmentService.createAppointmentServiceFunction(dto);

        return value ? "Appointment Create Successfully"
                : "Appointment Create Failed";
    }


    @PutMapping("/Update")
    @PreAuthorize("hasRole('PATIENT')")
    public String updateAppointmentServiceFunction(@RequestBody     UserAppointmentDTO userAppointmentDTO){
        boolean value=userAppointmentService.updateAppointmentServiceFunction(userAppointmentDTO);
        if(value==true){
            return "Update Successful";
        }
        else{
            return "Update Failed";
        }
    }

   @DeleteMapping("/Delete")
   @PreAuthorize("hasRole('PATIENT')")
    public String deleteAppointmentControllerFunction(@RequestParam int appointmentId){
        boolean value=userAppointmentService.deleteAppointmentServiceFunction(appointmentId);
        if(value){
            return "Deleted";
        }
        else{
            return  "failed";
        }
   }
   @GetMapping("/Get")
   @PreAuthorize("hasRole('PATIENT')")
   public UserAppointmentDTO getAppointmentByIdControllerFunction(@RequestParam int id){
        return userAppointmentService.getAppointmentByIdServiceFunction(id);
   }

   @GetMapping()
   @PreAuthorize("hasRole('PATIENT')")
    public List<UserAppointmentDTO>getAppointmentByIdControllerFunction(){
        return  userAppointmentService.getAppointmentServiceFunction();
   }


}
