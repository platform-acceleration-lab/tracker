package com.vmware.education.tracker;

import com.jayway.jsonpath.DocumentContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.jayway.jsonpath.JsonPath.parse;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TrackerApplication.class,
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ActuatorTests {
	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void testInfoEndpoint() {
		ResponseEntity<String> responseEntity =
				restTemplate.getForEntity("/actuator/info", String.class);

		DocumentContext infoJson = parse(responseEntity.getBody());

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(infoJson.read("$.git.commit.id", String.class)).isNotNull();
		assertThat(infoJson.read("$.build.artifact", String.class)).isEqualTo("tracker");
		assertThat(infoJson.read("$.build.name", String.class)).isEqualTo("tracker");
		assertThat(infoJson.read("$.build.group", String.class)).isEqualTo("com.vmware.education");
	}

	@Test
	public void testHealthEndpoint() {
		ResponseEntity<String> responseEntity =
				restTemplate.getForEntity("/actuator/health", String.class);

		DocumentContext healthCheckJson = parse(responseEntity.getBody());

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(healthCheckJson.read("$.status", String.class)).isEqualTo("UP");
		assertThat(healthCheckJson.read("$.components.db.status", String.class)).isEqualTo("UP");
	}

}
