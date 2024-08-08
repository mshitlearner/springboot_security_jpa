package in.mshitlearner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringbootJwtJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootJwtJpaApplication.class, args);
		
		//BCryptPasswordEncoder enc = new BCryptPasswordEncoder();
		//System.out.println(enc.encode("Password@1"));
		
	}

}
