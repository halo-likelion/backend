package likelion.halo.hamso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableJpaRepositories
public class HamsoApplication {

	public static void main(String[] args) {
		SpringApplication.run(HamsoApplication.class, args);
	}

}
