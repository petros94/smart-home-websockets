package com.pmitseas.control.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ActionDTO implements Serializable {
	private String destination;
	private String command;
	private Map<String, String> args;
}
