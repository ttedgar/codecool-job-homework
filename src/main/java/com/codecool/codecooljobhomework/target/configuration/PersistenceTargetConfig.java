package com.codecool.codecooljobhomework.target.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@PropertySource("classpath:application.properties")
@EnableJpaRepositories(
        basePackages = "com.codecool.codecooljobhomework.target.repository",
        entityManagerFactoryRef = "targetEntityManager",
        transactionManagerRef = "targetTransactionManager"
)
public class PersistenceTargetConfig {
    @Autowired
    private Environment env;

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean targetEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(targetDataSource());
        em.setPackagesToScan("com.codecool.codecooljobhomework.target.entity");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();

        properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
        properties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));

        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean
    @Primary
    public DataSource targetDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.target-datasource.driver-class-name"));
        dataSource.setUrl(env.getProperty("spring.target-datasource.url"));
        dataSource.setUsername(env.getProperty("spring.target-datasource.username"));
        dataSource.setPassword(env.getProperty("spring.target-datasource.password"));
        return dataSource;
    }

    @Bean
    @Primary
    public PlatformTransactionManager targetTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(targetEntityManager().getObject());
        return transactionManager;
    }
}
