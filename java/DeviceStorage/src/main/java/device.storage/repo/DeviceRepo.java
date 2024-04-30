package device.storage.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class DeviceRepo implements RowMapper<DeviceEntity> {
    public JdbcTemplate jdbcTemplate;

    @Autowired
    public DeviceRepo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public DeviceEntity getDevicesByName(String name) {
        String sql = "SELECT device_name, device_password FROM device WHERE device_name = ?";
        return jdbcTemplate.queryForObject(sql, this, name);
    }

    public void addDevice(String name, String password) {
        String sql = "INSERT INTO device (device_name, device_password) VALUES (?, ?)";
        jdbcTemplate.update(sql, name, password);
    }

    @Override
    public DeviceEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new DeviceEntity(
                rs.getString("device_name"),
                rs.getString("device_password")
        );
    }
}
