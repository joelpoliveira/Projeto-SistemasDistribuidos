package projeto.sd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("projeto.sd.*")
@EntityScan("projeto.sd.*")
@EnableAutoConfiguration
public class SdApplication {

	public static void main(String[] args) {
		SpringApplication.run(SdApplication.class, args);
	}

}
