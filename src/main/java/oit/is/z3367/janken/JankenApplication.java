package oit.is.z3367.janken;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync // @Asyncをつけたメソッドを非同期に実行できるようにする
@SpringBootApplication
public class JankenApplication {

	public static void main(String[] args) {
		SpringApplication.run(JankenApplication.class, args);
	}

}
