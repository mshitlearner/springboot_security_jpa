package in.mshitlearner.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.cors.reactive.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsServiceInfo();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		/*
		return httpSecurity.csrf(csrf -> csrf.disable())
					.authorizeHttpRequests( authorize -> {
								authorize.requestMatchers(HttpMethod.POST, "/courses/addCourse").authenticated()
												.requestMatchers("/courses/getAllCourses").permitAll()
												.requestMatchers("/courses/**").authenticated();
					}).build();  */
		
		httpSecurity.csrf(csrf -> csrf.disable());
		httpSecurity.cors(corsCustomizer -> {
		            corsCustomizer.configurationSource(corsConfigurationSource());
		        });
		httpSecurity.authorizeHttpRequests(authorize -> { authorize.anyRequest().fullyAuthenticated(); });
		httpSecurity.httpBasic(httpBasicCustomizer -> {
            httpBasicCustomizer
            .realmName("MyAppCustomer")
            .authenticationEntryPoint(authenticationEntryPoint());
		 });
		/*
		httpSecurity.formLogin(formLoginCustomizer -> {
	            formLoginCustomizer
	                .loginPage("/login")  // Custom login page
	                .loginProcessingUrl("/perform_login")  // URL to submit the username and password
	                .defaultSuccessUrl("/home", true)  // Redirect after successful login
	                .failureUrl("/login?error=true")  // Redirect after failed login
	                .permitAll();  // Allow access to the login page without authentication
	        });
 		*/
		return httpSecurity.build(); 
	}
	
    @Bean
    public BasicAuthenticationEntryPoint authenticationEntryPoint() {
        var entryPoint = new BasicAuthenticationEntryPoint();
        entryPoint.setRealmName("MyAppCustomer");
        return entryPoint;
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:9090"); // Allow specific origin
        configuration.addAllowedMethod("*"); // Allow all HTTP methods
        configuration.addAllowedHeader("*"); // Allow all headers
        configuration.setAllowCredentials(true); // Allow credentials

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply to all paths
        return source;
    }
    
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

	
}
