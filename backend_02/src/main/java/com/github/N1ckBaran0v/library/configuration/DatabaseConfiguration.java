package com.github.N1ckBaran0v.library.configuration;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;

@Configuration
@EnableJpaRepositories("com.github.N1ckBaran0v.library")
@EnableTransactionManagement
@PropertySource({"classpath:database.properties", "classpath:hibernate.properties"})
public class DatabaseConfiguration {
    private final Environment env;

    @Autowired
    public DatabaseConfiguration(@NotNull Environment env) {
        this.env = env;
    }

    @Bean
    public DataSource dataSource() {
        var dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Objects.requireNonNull(env.getProperty("database.driver")));
        dataSource.setUrl(env.getProperty("database.url"));
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@NotNull DataSource dataSource) {
        var adapter = new HibernateJpaVendorAdapter();
        adapter.setGenerateDdl(true);
        var factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setPackagesToScan("com.github.N1ckBaran0v.library");
        factory.setJpaVendorAdapter(adapter);
        factory.setJpaProperties(getHibernateProperties());
        return factory;
    }

    private Properties getHibernateProperties() {
        var properties = new Properties();
        properties.put("hibernate.connection.driver_class", env.getProperty("database.driver"));
        properties.put("hibernate.connection.url", env.getProperty("database.url"));
        properties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        properties.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        properties.put("hibernate.connection.isolation", env.getProperty("hibernate.connection.isolation"));
        return properties;
    }

    @Bean
    public PlatformTransactionManager transactionManager(@NotNull LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        var transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
        return transactionManager;
    }
}
