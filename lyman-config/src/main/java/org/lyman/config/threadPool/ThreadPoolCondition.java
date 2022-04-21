package org.lyman.config.threadPool;

import org.lyman.config.ConfigurationTrigger;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class ThreadPoolCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return ConfigurationTrigger.THREAD_POOL_ENABLE;
    }

}
