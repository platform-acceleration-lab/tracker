package com.vmware.education.tracker;

import com.jayway.jsonpath.DocumentContext;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.jayway.jsonpath.JsonPath.parse;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TrackerMigrationApplication.class,
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MigrationTests {
	@Autowired
	private TestRestTemplate restTemplate;
	@Autowired
	private Flyway flyway;

	@BeforeEach
	public void setUp() {
		flyway.clean();
	}

	@Test
	public void verifyMigrations() {
		flyway.migrate();

		assert (flyway.validateWithResult().validationSuccessful);
	}

	@Test
	public void verifyActuatorEndpoint() {
		flyway.migrate();

		ResponseEntity<String> responseEntity =
				restTemplate.getForEntity("/actuator/flyway", String.class);

		DocumentContext migrationJson = parse(responseEntity.getBody());

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(migrationJson.read(
				"$.contexts.tracker-migration.flywayBeans.flyway.migrations",
				String[].class))
				.isEmpty();
	}
}
