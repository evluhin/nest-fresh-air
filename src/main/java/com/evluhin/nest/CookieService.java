package com.evluhin.nest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;

/**
 * Cookie service.
 *
 */
@Component
public class CookieService {

	public static final String NEST_TOKEN = "nest-token";

	/**
	 * Create cookie to store tokenId.
	 *
	 * @param tokenId the token id
	 * @return the cookie
	 */
	public Cookie createCookie(String tokenId) {
		return createCookie(tokenId, null, null);
	}

	/**
	 * Create cookie to store tokenId, domain and path.
	 *
	 * @param tokenId the token id
	 * @param domain the domain (can be empty)
	 * @param path the path (can be empty)
	 * @return the cookie
	 */
	public Cookie createCookie(String tokenId, String domain, String path) {
		Cookie tokenCookie = new Cookie(NEST_TOKEN, tokenId);
		tokenCookie.setPath(StringUtils.defaultString(path, "/"));
		tokenCookie.setMaxAge(Integer.MAX_VALUE);
		tokenCookie.setSecure(true);
		if (domain != null) {
			tokenCookie.setDomain(domain);
		}
		return tokenCookie;
	}

}
