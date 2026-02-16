package App.Hospital.Appointment.Repository;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import App.Hospital.Appointment.DTO.UserDoctorDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserDoctorRepo {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<UserDoctorDTO> getAllDoctors() {


        String sql = "SELECT id, name, specialization, is_available FROM doctors";

        return jdbcTemplate.query(sql, new RowMapper<UserDoctorDTO>() {
            @Override
            public UserDoctorDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                UserDoctorDTO dto = new UserDoctorDTO();
                dto.setId(rs.getInt("id"));
                dto.setName(rs.getString("name"));
                dto.setSpecialization(rs.getString("specialization"));
                dto.setIsAvailable(rs.getBoolean("is_available"));
                return dto;
            }
        });
    }


    public boolean doctorAvailable(int id) {
        String sql = "SELECT is_available FROM doctors WHERE id = ?";
        Boolean available = jdbcTemplate.queryForObject(sql, Boolean.class, id); // <-- pass id here
        return available != null && available;
    }


}
