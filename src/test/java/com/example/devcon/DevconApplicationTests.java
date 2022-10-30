package com.example.devcon;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class DevconApplicationTests {

	@Test
	void contextLoads() {
		String pass = new BCryptPasswordEncoder().encode("pass");
		System.out.println(pass);
	}

}
