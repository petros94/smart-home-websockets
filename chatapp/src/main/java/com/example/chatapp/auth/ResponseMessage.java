package com.example.chatapp.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ResponseMessage {

	@JsonProperty("OutputTimeStamp")
	private String outputTimeStamp;
	@JsonProperty("StatusCode")
	private String statusCode;
	@JsonProperty("StatusMessage")
	private String statusMessage;

}
