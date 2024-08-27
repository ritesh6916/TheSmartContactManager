package info.ritesh.scm;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import info.ritesh.scm.services.EmailService;

@SpringBootTest
class TheSmartContactManagerApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private EmailService emailService;

	@Test
	void sendEmailTest() throws Exception{
		emailService.sendEmail("the.developer.ritesh@gmail.com", "Verify THCM ", "Testing Emial service with THCM");
	}

}
