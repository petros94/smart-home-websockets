
package com.example.chatapp.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AMSUser implements Serializable {

	@JsonProperty("Body")
	private Body body;
	@JsonProperty("ResponseMessage")
	private ResponseMessage responseMessage;

}
