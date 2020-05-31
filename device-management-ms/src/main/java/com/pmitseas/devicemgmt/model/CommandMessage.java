package com.pmitseas.devicemgmt.model;

import lombok.*;

import java.io.Serializable;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class CommandMessage implements Serializable {
    private String time;
    private String command;
    private Map<String, String> args;
}
