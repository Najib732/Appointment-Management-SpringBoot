package App.Hospital.Appointment.Repository;

import App.Hospital.Appointment.DTO.UserDTO;
import App.Hospital.Appointment.DTO.UserLoginDTO;
import App.Hospital.Appointment.DTO.UserRegistrationDTO;
import App.Hospital.Appointment.Interface.UserInterface;

import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepo implements UserInterface {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private PasswordEncoder passwordEncoder; // Spring bean inject

    @Override
    public String Registration(UserRegistrationDTO dto) {

        String checkSql = "SELECT COUNT(*) FROM users WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(checkSql, new Object[]{dto.getEmail()}, Integer.class);

        if (count != null && count > 0) {
            return "Email already registered!";
        }

        String sql = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)";
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        System.out.println(encodedPassword);

        jdbcTemplate.update(sql,
                dto.getName(),
                dto.getEmail(),
                encodedPassword,
                dto.getRole()
        );

        return "User registered successfully: " + dto.getEmail();
    }

    @Override
    public String UpdatePassword(String email, String password) {
        String sql = "UPDATE users SET password = ? WHERE email = ?";

        int rowsAffected = jdbcTemplate.update(sql, password, email);

        if (rowsAffected > 0) {
            return "successful";
        }
        return "error";
    }


    public boolean userAvailable(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql,new Object[]{email},Integer.class);
        return count != null && count > 0;
    }

    @Override
    public boolean ForgetPassword(String gmail, String password) {
        String sql = "UPDATE users SET password = ? WHERE email = ?";
        int rowsAffected = jdbcTemplate.update(sql, password, gmail);
        return rowsAffected > 0;
    }


    @Override
    public UserDTO UserInfo(String email) {
        String sql = "SELECT id, name, email, role FROM users WHERE email = ?";
        UserDTO user = jdbcTemplate.queryForObject(
                sql,new Object[]{email},
                (rs, rowNum) -> {
                    UserDTO dto = new UserDTO();
                    dto.setId(rs.getInt("id"));
                    dto.setName(rs.getString("name"));
                    dto.setEmail(rs.getString("email"));
                    dto.setRole(rs.getString("role"));
                    return dto;
                }
        );
        return user;
    }

}
