package org.devgateway.toolkit.persistence.spring;

import liquibase.integration.spring.SpringLiquibase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Component;

/**
 * @author Octavian Ciubotaru
 */
@Component
public class LiquibaseAfterJPARunner implements BeanPostProcessor {

    private final Logger logger = LoggerFactory.getLogger(LiquibaseAfterJPARunner.class);

    private SpringLiquibase springLiquibase;
    private LocalContainerEntityManagerFactoryBean emFactoryBean;
    private boolean alreadyExecuted;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof LocalContainerEntityManagerFactoryBean) {
            emFactoryBean = (LocalContainerEntityManagerFactoryBean) bean;
        }
        if (bean instanceof SpringLiquibase) {
            springLiquibase = (SpringLiquibase) bean;
        }
        if (!alreadyExecuted && emFactoryBean != null && springLiquibase != null) {
            runLiquibaseAfterHbm2Ddl();
        }
        return bean;
    }

    private void runLiquibaseAfterHbm2Ddl() {
        try {
            logger.info("Executing liquibase after hbm2ddl...");
            springLiquibase.afterPropertiesSet();
        } catch (Exception e) {
            throw new BeanCreationException("Failed to execute liquibase after JPA", e);
        } finally {
            alreadyExecuted = true;
            emFactoryBean = null;
            springLiquibase = null;
        }
    }
}
