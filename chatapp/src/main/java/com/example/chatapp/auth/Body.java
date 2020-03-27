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
public class Body {

	@JsonProperty("access_token")
	private String accessToken;
	@JsonProperty(".expires")
	private String expires;
	@JsonProperty("expires_in")
	private String expiresIn;
	@JsonProperty(".issued")
	private String issued;
	@JsonProperty("token_type")
	private String tokenType;
	@JsonProperty("userDetails")
	private UserDetails userDetails;
	@JsonProperty("userName")
	private String userName;
	@JsonProperty("userOrganisationUnit")
	private String userOrganisationUnit;
	@JsonProperty("userRoles")
	private String userRoles;
	@JsonProperty("success")
	private boolean success;

}
