package App.Hospital.Appointment.Service;

import App.Hospital.Appointment.DTO.*;
import App.Hospital.Appointment.Repository.UserRepo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    UserJWTService userJwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    UserEmailService userEmailService;


    public String verify(UserLoginDTO userLoginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginDTO.getEmail(), userLoginDTO.getPassword())
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String role = userDetails.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .map(r -> r.replace("ROLE_", ""))
                .map(r -> r.substring(0, 1).toUpperCase() + r.substring(1).toLowerCase())
                .findFirst()
                .orElse("Patient");
        if(authentication.isAuthenticated()) {
         return  userJwtService.generateToken(userLoginDTO.getEmail(),role);
        }
        return "Fail";
    }
    public String RegistrationServiceFunction(UserRegistrationDTO userRegistrationDTO){
        return userRepo.Registration(userRegistrationDTO);
    }


    public String UpdatePasswordServiceFunction(UserUpdateDTO userUpdateDTO){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        String encodedPassword = passwordEncoder.encode(userUpdateDTO.getPassword());
       return userRepo.UpdatePassword(email,encodedPassword);
    }




    public String ForgetPasswordServiceFunction(UserForgetPassDTO userForgetPassDTO) {
        String email = userForgetPassDTO.getEmail();

        if (!userRepo.userAvailable(email)) {
            throw new IllegalStateException("No user found with email: " + email);
        }

        String encodedPassword = passwordEncoder.encode(userForgetPassDTO.getPassword());
        boolean updated = userRepo.ForgetPassword(email, encodedPassword);

        if (!updated) {
            throw new RuntimeException("Failed to reset password for email: " + email);
        }

        String subject = "Password Recovery";
        String body = "Your password has been successfully changed.";
        userEmailService.sendSimpleEmail(email, subject, body);

        return   "Password reset successfully for email: " + email;
    }



    public UserDTO ProfileInfoServiceFunction() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        UserDTO user = userRepo.UserInfo(email);
        if (user == null) {
            throw new IllegalStateException("User not found with email: " + email);
        }

        return user;
    }
}
