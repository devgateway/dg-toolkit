package org.devgateway.toolkit.persistence.test;

import org.devgateway.toolkit.persistence.spring.PersistenceApplication;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author mpostelnicu
 *
 */
@ExtendWith(SpringExtension.class)
@ActiveProfiles("integration")
@SpringBootTest(classes = { PersistenceApplication.class })
@TestPropertySource("classpath:test.properties")
public abstract class AbstractPersistenceTest {

}
