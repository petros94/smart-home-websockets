package com.pmitseas.devicemgmt.model;

import lombok.*;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CommandMessage implements Serializable {
	private UUID id;
	private String time;
	private String command;
	private Map<String, String> args;
}
