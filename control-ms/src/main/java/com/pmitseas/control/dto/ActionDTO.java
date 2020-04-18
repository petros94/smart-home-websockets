package com.pmitseas.control.dto;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
@ToString
public class ActionDTO implements Serializable {
	 private String destination;
}
