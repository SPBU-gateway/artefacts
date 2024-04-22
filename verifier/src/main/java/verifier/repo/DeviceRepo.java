package verifier.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DeviceRepo {
    public JdbcTemplate jdbcTemplate;

    @Autowired
    public DeviceRepo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String getPasswordByName(String name) {
        String sql = "SELECT device_password FROM device WHERE device_name = ?";
        return jdbcTemplate.queryForObject(sql, String.class, name);
    }

    public void addDevice(String name, String password) {
        String sql = "INSERT INTO device (device_name, device_password) VALUES (?, ?)";
        jdbcTemplate.update(sql, name, password);
    }
}
