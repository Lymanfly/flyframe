package org.lyman.config.mongodb;

import org.lyman.config.ConfigurationTrigger;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class MongodbCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return ConfigurationTrigger.MONGODB_ENABLE;
    }

}
