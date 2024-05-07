package manager.input.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewDeviceDTO {
    private String clientName;
    private String deviceName;
    private String devicePassword;
}
