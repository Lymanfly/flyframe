package org.lyman.config;

import org.lyman.utils.SystemUtils;

public interface ConfigurationTrigger {

    boolean HIBERNATE_ENABLE = SystemUtils.getPropertyBoolean("lyamn.switch.hibernate");

    boolean REST_ENABLE = SystemUtils.getPropertyBoolean("lyamn.switch.rest");

    boolean THREAD_POOL_ENABLE = SystemUtils.getPropertyBoolean("lyamn.switch.thread-pool");

    boolean VALIDATION_ENABLE = SystemUtils.getPropertyBoolean("lyamn.switch.validation");

    boolean MONGODB_ENABLE = SystemUtils.getPropertyBoolean("lyamn.switch.mongodb");

}
