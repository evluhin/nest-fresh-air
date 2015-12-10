package com.evluhin.nest.security;

//@EnableOAuth2Resource
//@Configuration
public class ResourceServer {// extends WebSecurityConfigurerAdapter {

	//	@Autowired
	//	NestProperties properties;
	//
	//	@Bean
	//	public ResourceServerTokenServices tokenService() {
	//		RemoteTokenServices tokenServices = new RemoteTokenServices();
	//		tokenServices.setClientId(properties.getId());
	//		tokenServices.setClientSecret(properties.getSecret());
	//		tokenServices.setTokenName("tokenName");
	//		tokenServices.setCheckTokenEndpointUrl("http://localhost:8080/oauth/check_token");
	//		return tokenServices;
	//	}
	//
	//	@Override
	//	public AuthenticationManager authenticationManagerBean() throws Exception {
	//		OAuth2AuthenticationManager authenticationManager = new OAuth2AuthenticationManager();
	//		authenticationManager.setTokenServices(tokenService());
	//		return authenticationManager;
	//	}
	//
	//@Configuration
	//@EnableResourceServer
	//    protected static class ResourceServerConfig extends ResourceServerConfigurerAdapter {
	//
	//    @Override
	//    public void configure(HttpSecurity http) throws Exception {
	//        http
	//            .requestMatchers()
	//            .antMatchers("/","/home")
	//            .and()
	//            .authorizeRequests()
	//            .anyRequest().access("#oauth2.hasScope('read')");
	//    }
	//
	//    @Override
	//    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
	//        TokenStore tokenStore = new InMemoryTokenStore();
	//        resources.resourceId("Resource Server");
	//        resources.tokenStore(tokenStore);
	//    }
}