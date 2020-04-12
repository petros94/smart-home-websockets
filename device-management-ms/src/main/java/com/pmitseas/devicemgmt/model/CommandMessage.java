package com.pmitseas.devicemgmt.model;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class CommandMessage implements Serializable {
    private LocalDateTime time;
    private String command;
    private Map<String, String> args;
}
