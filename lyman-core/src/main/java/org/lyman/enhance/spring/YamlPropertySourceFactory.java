package org.lyman.enhance.spring;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.IOException;
import java.util.Properties;

public class YamlPropertySourceFactory implements PropertySourceFactory {

    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        Resource r = resource.getResource();
        String filename = r.getFile().getName().toLowerCase();
        if (filename.endsWith("yml") || filename.endsWith("yaml")) {
            YamlPropertiesFactoryBean bean = new YamlPropertiesFactoryBean();
            bean.setResources(r);
            bean.afterPropertiesSet();
            Properties properties = bean.getObject();
            PropertySource propertySource = new PropertiesPropertySource(name == null ? filename : name, properties);
            return propertySource;
        }
        return null;
    }

}
