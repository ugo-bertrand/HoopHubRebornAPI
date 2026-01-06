package fr.hoophub.hoophubAPI;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class HoophubApiApplicationTests {

	@Autowired
	private HoophubApiApplication application;

	@Test
	void loadApplicationTest() {
		assertThat(application).isNotNull();
	}

}
