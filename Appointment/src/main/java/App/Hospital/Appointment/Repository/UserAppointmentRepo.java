package App.Hospital.Appointment.Repository;


import App.Hospital.Appointment.DTO.UserAppointmentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Repository
public class UserAppointmentRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public boolean isAppointmentBooked(int patientId, int doctorId, LocalDate appointmentDate, LocalTime appointmentTime) {
        String sql = "SELECT COUNT(*) FROM appointments " +
                "WHERE patient_id = ? AND doctor_id = ? AND appointment_date = ? AND appointment_time = ?";

        Integer count = jdbcTemplate.queryForObject(
                sql,
                new Object[]{
                        patientId,
                        doctorId,
                        java.sql.Date.valueOf(appointmentDate),
                        java.sql.Time.valueOf(appointmentTime)
                },
                Integer.class
        );

        return count != null && count > 0;
    }

    public UserAppointmentDTO getAppointmentByAppointmentId(int id) {
        String sql = "SELECT * FROM appointments WHERE id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, new RowMapper<UserAppointmentDTO>() {
                @Override
                public UserAppointmentDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                    UserAppointmentDTO dto = new UserAppointmentDTO();
                    dto.setAppointmentId(rs.getInt("id"));
                    dto.setPatientId(rs.getInt("patient_id"));
                    dto.setDoctorId(rs.getInt("doctor_id"));
                    dto.setAppointmentDate(rs.getDate("appointment_date").toLocalDate());
                    dto.setAppointmentTime(rs.getTime("appointment_time").toLocalTime());
                    dto.setStatus(rs.getString("status"));
                    return dto;
                }
            });
        } catch (Exception e) {
            return null;
        }
    }

    public UserAppointmentDTO insertAppointmentAndReturnId(UserAppointmentDTO userAppointmentDTO) {

        String sql = "INSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_time) " +
                "VALUES (?, ?, ?, ?) RETURNING id";

        Integer id = jdbcTemplate.queryForObject(
                sql,
                new Object[]{
                        userAppointmentDTO.getPatientId(),
                        userAppointmentDTO.getDoctorId(),
                        userAppointmentDTO.getAppointmentDate(),
                        userAppointmentDTO.getAppointmentTime()
                },
                Integer.class
        );

        if (id == null) {
            throw new IllegalStateException("Failed to create appointment");
        }

        userAppointmentDTO.setAppointmentId(id);
        return userAppointmentDTO;
    }


    public UserAppointmentDTO createAppointment(UserAppointmentDTO userAppointmentDTO) {

        String sql = "INSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_time) VALUES (?, ?, ?, ?)";
        int rows = jdbcTemplate.update(sql,
                userAppointmentDTO.getPatientId(),
                userAppointmentDTO.getDoctorId(),
                userAppointmentDTO.getAppointmentDate(),
                userAppointmentDTO.getAppointmentTime());

        if (rows > 0) {
            return userAppointmentDTO;
        }

        return null;
    }


    public boolean updateAppointment(UserAppointmentDTO userAppointmentDTO) {

        String sql = "UPDATE appointments SET patient_id = ?, doctor_id = ?,appointment_date = ?,appointment_time = ? WHERE id = ?";


        int rows = jdbcTemplate.update(
                sql,
                userAppointmentDTO.getPatientId(),
                userAppointmentDTO.getDoctorId(),
                java.sql.Date.valueOf(userAppointmentDTO.getAppointmentDate()),
                java.sql.Time.valueOf(userAppointmentDTO.getAppointmentTime()),
                userAppointmentDTO.getAppointmentId()
        );

        return rows > 0;
    }


    public boolean deleteAppointment(int appointmentId) {
        String sql = "DELETE FROM appointments WHERE id = ?";
        int rows = jdbcTemplate.update(sql, appointmentId);
        return rows > 0;
    }


    public UserAppointmentDTO getAppointmentById(int appointmentId, int patientId) {
        String sql = "SELECT * FROM appointments WHERE id = ? AND patient_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{appointmentId, patientId}, (rs, rowNum) -> {
                UserAppointmentDTO dto = new UserAppointmentDTO();
                dto.setAppointmentId(rs.getInt("id"));
                dto.setPatientId(rs.getInt("patient_id"));
                dto.setDoctorId(rs.getInt("doctor_id"));
                dto.setAppointmentDate(rs.getDate("appointment_date").toLocalDate());
                dto.setAppointmentTime(rs.getTime("appointment_time").toLocalTime());
                dto.setStatus(rs.getString("status"));
                return dto;
            });
        } catch (Exception e) {
            return null;
        }
    }


    public List<UserAppointmentDTO> getAllAppointments(int patientid) {
        String sql = "SELECT * FROM appointments where patient_id=? ";

        return jdbcTemplate.query(sql, new Object[]{patientid}, (rs, rowNum) -> {
            UserAppointmentDTO dto = new UserAppointmentDTO();
            dto.setAppointmentId(rs.getInt("id"));
            dto.setPatientId(rs.getInt("patient_id"));
            dto.setDoctorId(rs.getInt("doctor_id"));
            dto.setAppointmentDate(rs.getDate("appointment_date").toLocalDate());
            dto.setAppointmentTime(rs.getTime("appointment_time").toLocalTime());
            dto.setStatus(rs.getString("status"));
            return dto;
        });
    }

}
