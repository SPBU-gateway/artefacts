package main.manager.output.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageAuth {
    private String from;
    private String to;
    private String address;
    private List<Device> devices;
}