package com.example.chatapp.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserDetails {

	@JsonProperty("authenticationName")
	private String authenticationName;
	@JsonProperty("branchBg")
	private String branchBg;
	@JsonProperty("chiefDepartmentBg")
	private String chiefDepartmentBg;
	@JsonProperty("department")
	private String department;
	@JsonProperty("departmentBg")
	private String departmentBg;
	@JsonProperty("displayName")
	private String displayName;
	@JsonProperty("division")
	private String division;
	@JsonProperty("divisionBg")
	private String divisionBg;
	@JsonProperty("eMail")
	private String eMail;
	@JsonProperty("eln")
	private String eln;
	@JsonProperty("familyName")
	private String familyName;
	@JsonProperty("firstName")
	private String firstName;
	@JsonProperty("fullName")
	private String fullName;
	@JsonProperty("groups")
	private transient List<String> mGroups;
	@JsonProperty("isDeleted")
	private Boolean isDeleted;
	@JsonProperty("manager")
	private String manager;
	@JsonProperty("mobileNumber")
	private String mobileNumber;
	@JsonProperty("physicalDeliveryOfficeName")
	private transient String physicalDeliveryOfficeName;
	@JsonProperty("position")
	private String position;
	@JsonProperty("positionBg")
	private String positionBg;
	@JsonProperty("postOfficeBox")
	private String postOfficeBox;
	@JsonProperty("postOfficeCity")
	private String postOfficeCity;
	@JsonProperty("postOfficeCityBg")
	private String postOfficeCityBg;
	@JsonProperty("postOfficeStreetAddress")
	private String postOfficeStreetAddress;
	@JsonProperty("sectionBg")
	private String sectionBg;
	@JsonProperty("sid")
	private String sid;
	@JsonProperty("substituteUser")
	private String substituteUser;
	@JsonProperty("substituteForUsers")
	private List<String> substituteForUsers;
	@JsonProperty("surname")
	private String surname;
	@JsonProperty("userRoles")
	private List<String> userRoles;
	@JsonProperty("username")
	private String username;

}
