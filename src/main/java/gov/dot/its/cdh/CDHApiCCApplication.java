package gov.dot.its.cdh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ConfigProperties.class)
public class CDHApiCCApplication {

	public static void main(String[] args) {
		SpringApplication.run(CDHApiCCApplication.class, args);
	}

}
