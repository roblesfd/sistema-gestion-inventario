package com.roblez.inventorysystem;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = SgiApplication.class)
@ActiveProfiles("test")
class SgiApplicationTests {

	@Test
	void contextLoads() {
	}

}
