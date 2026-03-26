package com.brunofragadev;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled("Desativado porque exige banco de dados real no CI")
class BrunofragadevApplicationTests {

	@Test
	void contextLoads() {
	}

}
