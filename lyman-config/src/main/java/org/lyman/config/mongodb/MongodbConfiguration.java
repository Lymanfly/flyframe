package org.lyman.config.mongodb;

import com.mongodb.client.MongoClient;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Conditional(MongodbCondition.class)
@Configuration
public class MongodbConfiguration {

    public MongoClient mongoClient() {
        return null;
    }

}
