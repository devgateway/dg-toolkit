package org.devgateway.toolkit.web;

import org.devgateway.toolkit.web.spring.WebApplication;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("integration")
@SpringBootTest(classes = { WebApplication.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public abstract class AbstractWebTest {

}
