package com.roblez.inventorysystem;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.roblez.inventorysystem.security.JwtUtil;

@SpringBootTest(classes = SgiApplication.class)
@ActiveProfiles("test")
class SgiApplicationTests {
	
    @MockitoBean
    private JwtUtil jwtUtil; 


	@Test
	void contextLoads() {
	}

}
