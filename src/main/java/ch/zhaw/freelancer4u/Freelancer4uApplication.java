package ch.zhaw.freelancer4u;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import ch.zhaw.freelancer4u.model.MailCredentials;


@SpringBootApplication
public class Freelancer4uApplication {

	public static void main(String[] args) {
		SpringApplication.run(Freelancer4uApplication.class, args);
	}
}
