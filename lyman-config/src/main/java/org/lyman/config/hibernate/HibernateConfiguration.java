package org.lyman.config.hibernate;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.lyman.enhance.hibernate.LymanDao;
import org.lyman.utils.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;

@Slf4j(topic = "HibernateConfiguration")
@Configuration
@PropertySource(value = "classpath:application.yml")
public class HibernateConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "hibernate.lyman")
    public SessionFactoryProperties sessionFactoryProperties() {
        SessionFactoryProperties properties = new SessionFactoryProperties();
        return properties;
    }

    @Bean
    @Autowired
    public LocalSessionFactoryBean sessionFactoryBean(DataSource dataSource,
                                                      SessionFactoryProperties properties) {
        Map<String, Object> map = properties.getHibernateProperties();
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setPackagesToScan(properties.getPackagesToScan());
        if (CollectionUtils.isNotEmpty(map)) {
            Properties props = new Properties();
            props.putAll(map);
            sessionFactoryBean.setHibernateProperties(props);
        }
        return sessionFactoryBean;
    }

    @Bean
    @Autowired
    public PlatformTransactionManager hibernateTransactionManager(LocalSessionFactoryBean sessionFactoryBean) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactoryBean.getObject());
        return transactionManager;
    }

    @Bean
    @Autowired
    public LymanDao getDataAccessObject(SessionFactory sessionFactory) {
        LymanDao dao = new LymanDao();
        dao.setSessionFactory(sessionFactory);
        return dao;
    }

    @Data
    private static class SessionFactoryProperties {
        private Map<String, Object> hibernateProperties;
        private String packagesToScan;
    }

}
