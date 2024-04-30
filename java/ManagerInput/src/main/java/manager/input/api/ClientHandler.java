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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ClientHandler {
    private final ToClientAuthNewDevice prodNewDevice;
    private final ToClientAuth prod;

    @PostMapping("/new-device")
    public ResponseEntity<String> NewDevice(
            final @RequestBody NewDeviceDTO request
    ){
        log.info("New device request received: {}, {}, {}", request.getClientName(), request.getDeviceName(), request.getDevicePassword());
        try{
            prodNewDevice.send(
                    new GetCredentialsNewDevice(request.getClientName(), new Device(request.getDeviceName(), request.getDevicePassword())),
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

    @PostMapping("/get-devices")
    public ResponseEntity<String> requestGetDevices(
            final @RequestBody GetDevicesDTO request
    ){
        log.info("Get devices request received: {}, {}", request.getClientName(), request.getAddress());
        try{
            prod.send(
                    new GetDevices(request.getClientName(), request.getAddress()),
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
