package main.manager.output.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Device {
    private String from;
    private String to;
    private String name;
    private String password;
    private String message;
}
