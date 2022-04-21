package org.lyman.utils.download;

import lombok.extern.slf4j.Slf4j;
import org.lyman.utils.CollectionUtils;
import org.lyman.utils.StringUtils;
import org.springframework.beans.factory.config.YamlMapFactoryBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.InputStreamResource;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Slf4j(topic = "ContentType initializer")
@SuppressWarnings("unchecked")
@Configuration
public class ContentType {

    private static String defaultType = "application/octet-stream";

    private static Map<String, String> types = new HashMap<>();

    static {
        InputStream is = ContentType.class.getClassLoader()
                .getResourceAsStream("content-type.yml");
        if (is != null) {
            InputStreamResource resource = new InputStreamResource(is);
            YamlMapFactoryBean bean = new YamlMapFactoryBean();
            bean.setResources(resource);
            bean.afterPropertiesSet();
            Map<String, Object> map = bean.getObject();
            if (CollectionUtils.isNotEmpty(map)) {
                defaultType = (String) map.getOrDefault("default", "application/octet-stream");
                types = (Map<String, String>) map.getOrDefault("mime-types", new HashMap<>());
            }
        } else
            log.error("content-type.yml not found for ContentType initializer");
    }

    public static String getContentType(String ext) {
        if (StringUtils.isEmpty(ext))
            return defaultType;
        return types.getOrDefault(ext.trim().toLowerCase(), defaultType);
    }

}
