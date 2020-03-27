package com.example.chatapp;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

  @Autowired
  private HttpServletRequest request;

  @Value("${ndp.rs.endpoint.rs714}")
  private String RS714url;

  @Value("${ndp.security.clientId}")
  private String clientId;

  private final RestTemplate restTemplate;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String username = authentication.getName();
    String password = authentication.getCredentials().toString();

//    AMSUser amsUser;
//
//    HttpHeaders headers = new HttpHeaders();
//    headers.set("Content-Type", "application/x-www-form-urlencoded");
//    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
//    map.add("client_id", "http://csasit.ad.btk.bg");
//    map.add("username", username);
//    map.add("grant_type", "password");
//    map.add("password", password);
//
//    HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
//    try {
//      restTemplate.postForObject(RS714url, entity, AMSUser.class);
//    } catch (RestClientResponseException e) {
//      throw new BadCredentialsException("Wrong username/password", e);
//    } catch (Exception e) {
//      throw new AuthenticationServiceException("Internal Server Error", e);
//    }
//    return new UsernamePasswordAuthenticationToken(request.getRemoteAddr(), password, new ArrayList<>());
    return new UsernamePasswordAuthenticationToken("192.168.5.143", password, new ArrayList<>());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}
