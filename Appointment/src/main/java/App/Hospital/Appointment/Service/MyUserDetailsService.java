


package App.Hospital.Appointment.Service;

import App.Hospital.Appointment.DTO.UserDTO;
import App.Hospital.Appointment.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService   {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        String sql = "SELECT * FROM users WHERE email = ?";
        UserDTO user;
        try {
            user = jdbcTemplate.queryForObject(sql, new Object[]{email}, (rs, rowNum) -> {
                UserDTO dto = new UserDTO();
                dto.setId(rs.getInt("id"));
                dto.setName(rs.getString("name"));
                dto.setEmail(rs.getString("email"));
                dto.setPassword(rs.getString("password"));
                dto.setRole(rs.getString("role"));
                return dto;
            });
        } catch (EmptyResultDataAccessException e) {
            throw new UsernameNotFoundException("User not found");
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }
}