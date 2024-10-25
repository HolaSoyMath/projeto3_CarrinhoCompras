package math.projeto3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class Projeto3Application {

	public static void main(String[] args) {
		SpringApplication.run(Projeto3Application.class, args);
	}

}
