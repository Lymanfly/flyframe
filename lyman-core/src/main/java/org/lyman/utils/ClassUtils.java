package org.lyman.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j(topic = "ClassUtils")
public class ClassUtils extends org.springframework.util.ClassUtils {

    private static final String CLASSPATH_SEPERATOR = "/";

    private static final String CLASS_RESOURCE_PATTERN = "**/*.class";

    public static List<String> findClasses(String packageToScan, ResourceMetadataFilter... filters) {
        if (StringUtils.isEmpty(packageToScan))
            return null;
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + convertClassNameToResourcePath(
                SpringContextUtils.getEnvironment().resolveRequiredPlaceholders(packageToScan)) + CLASSPATH_SEPERATOR
                + CLASS_RESOURCE_PATTERN;
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory();
        try {
            List<String> subClasses = new ArrayList<>();
            Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
            if (ArrayUtils.isEmpty(resources))
                return null;
            RESOURCE_SCOPE : for (Resource resource : resources) {
                if (resource.isReadable()) {
                    MetadataReader reader = metadataReaderFactory.getMetadataReader(resource);
                    if (ArrayUtils.isNotEmpty(filters)) {
                        for (ResourceMetadataFilter filter : filters) {
                            if (!filter.accept(reader))
                                continue RESOURCE_SCOPE;
                        }
                    }
                    subClasses.add(reader.getClassMetadata().getClassName());
                }
            }
            return subClasses;
        } catch (IOException e) {
            log.error("Failed to Load Resources within package : {}", packageSearchPath, e);
            return null;
        }
    }

    public interface ResourceMetadataFilter {

        boolean accept(MetadataReader metadataReader, Object... params);

    }

}
