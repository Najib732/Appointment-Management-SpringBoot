package App.Hospital.Appointment.Service;



import App.Hospital.Appointment.DTO.UserDoctorDTO;
import App.Hospital.Appointment.Repository.UserDoctorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDoctorService {

    @Autowired
    UserDoctorRepo userDoctorRepo;

    public List<UserDoctorDTO> getAllDoctors() {
        return userDoctorRepo.getAllDoctors();
    }
}
