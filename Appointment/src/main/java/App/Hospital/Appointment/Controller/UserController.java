package App.Hospital.Appointment.Controller;

import App.Hospital.Appointment.DTO.*;
import App.Hospital.Appointment.Service.UserEmailService;
import App.Hospital.Appointment.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@EnableMethodSecurity
@RestController
@RequestMapping("/User")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserEmailService emailService;


    @PostMapping("/Registration")
    public String RegistrationControllerFunction(@RequestBody @Valid UserRegistrationDTO userRegistrationDTO) {
        return userService.RegistrationServiceFunction(userRegistrationDTO);
    }

    @PostMapping("/Login")
    public String LoginControllerFunction(@RequestBody @Valid UserLoginDTO userLoginDTO) {
        return userService.verify(userLoginDTO);
    }

    @PutMapping("/Update")
    @PreAuthorize("hasRole('PATIENT')")
    public String UpdatePasswordControllerFunction( @Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        return userService.UpdatePasswordServiceFunction(userUpdateDTO);
    }


    @PutMapping("/ForgetPassowrd")
    @PreAuthorize("hasRole('PATIENT')")
    public String ForgetPasswordControllerFunction(@RequestBody @Valid UserForgetPassDTO userForgetPassDTO) {
        return userService.ForgetPasswordServiceFunction(userForgetPassDTO);
    }


    @GetMapping("/Info")
    @PreAuthorize("hasRole('PATIENT')")
    public UserDTO ProfileInfoContollerFunction() {
        return userService.ProfileInfoServiceFunction();
    }


}




