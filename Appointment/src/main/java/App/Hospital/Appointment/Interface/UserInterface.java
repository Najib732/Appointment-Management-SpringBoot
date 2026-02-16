package App.Hospital.Appointment.Interface;

import App.Hospital.Appointment.DTO.UserDTO;
import App.Hospital.Appointment.DTO.UserLoginDTO;
import App.Hospital.Appointment.DTO.UserRegistrationDTO;

public interface UserInterface {
    String Registration(UserRegistrationDTO RD);
    //String Login(UserLoginDTO userLoginDTO);
    String UpdatePassword(String email,String password);
    public boolean ForgetPassword(String gmail, String password);
    public UserDTO UserInfo(String email);
}
