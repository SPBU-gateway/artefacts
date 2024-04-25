package manager.input.api;

import manager.input.dto.Device;
import manager.input.dto.GetCredentialsNewDevice;
import manager.input.dto.GetDevices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import manager.input.producers.ToClientAuth;
import manager.input.producers.ToClientAuthNewDevice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ClientHandler {
    private final ToClientAuthNewDevice prodNewDevice;
    private final ToClientAuth prod;

    @GetMapping("/new-device")
    public ResponseEntity<String> NewDevice(
            final @RequestParam("client-name") String clientName,
            final @RequestParam("device-name") String deviceName,
            final @RequestParam("device-password") String devicePassword
    ){
        log.info("New device request received: {}, {}, {}", clientName, deviceName, devicePassword);
        try{
            prodNewDevice.send(
                    new GetCredentialsNewDevice(clientName, new Device(deviceName, devicePassword)),
                    "new-device",
                    "manager-input",
                    "client-auth"
            );
        }catch (Exception ex) {
            log.info("Error: {}", ex.getMessage());
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
        return ResponseEntity.ok("Success");
    }

    @GetMapping("/get-devices")
    public ResponseEntity<String> requestGetDevices(
            final @RequestParam("client-name") String clientName,
            final @RequestParam("address") String address
    ){
        log.info("Get devices request received: {}, {}", clientName, address);
        try{
            prod.send(
                    new GetDevices(clientName, address),
                    "default",
                    "manager-input",
                    "client-auth"
            );
        }catch (Exception ex) {
            log.info("Error: {}", ex.getMessage());
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
        return ResponseEntity.ok("Success");
    }
}
