package com.pmitseas.control.controller;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EchoController {

	@GetMapping("/random")
	public String random() {
		return RandomStringUtils.randomAlphabetic(10);
	}

}
