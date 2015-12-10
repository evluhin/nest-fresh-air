package com.evluhin.nest.controller;

import com.evluhin.nest.CookieService;
import com.evluhin.nest.NestProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 */
@Controller
public class CallbackController {

	@Autowired
	NestProperties properties;
	@Autowired
	private CookieService cookieService;

	@RequestMapping("/callback")
	public void callback(@RequestParam("code") String code, HttpServletResponse response) throws IOException {

		String url = properties.getRawTokenUrl();


		RestTemplate rest = new RestTemplate();
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();


		map.add("code", code);
		map.add("client_id", properties.getId());
		map.add("client_secret", properties.getSecret());
		map.add("grant_type", "authorization_code");

		TokenResponse result = rest.postForObject(url, map, TokenResponse.class);
		System.out.println(result);

		response.addCookie(cookieService.createCookie(result.getAccess_token()));


		response.sendRedirect("/");
	}


	static class TokenResponse {

		String access_token;
		Long expires_in;

		public TokenResponse() {
		}

		public String getAccess_token() {
			return access_token;
		}

		public void setAccess_token(String access_token) {
			this.access_token = access_token;
		}

		public Long getExpires_in() {
			return expires_in;
		}

		public void setExpires_in(Long expires_in) {
			this.expires_in = expires_in;
		}
	}

}
