package com.pmitseas.control.event;

import lombok.*;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ActionEvent implements Serializable {
     private UUID id;
     private String destination;
     private String command;
     private Map<String, String> args;
}
