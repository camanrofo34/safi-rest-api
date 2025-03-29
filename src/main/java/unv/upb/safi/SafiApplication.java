package unv.upb.safi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "unv.upb.safi")
public class SafiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SafiApplication.class, args);
	}

}
