package org.lyman.exceptions;

public interface LymanErrorCode {

    int ARGUMENT_ILLEGAL = 1000;
    int ARGUMENT_MISSSING = 1001;
    int TYPE_MISMATCH = 1002;

    int SYSTEM_ERROR = 2000;
    int PERMISSION_DENY = 2001;
    int NO_SESSION = 2002;
    int REQUEST_ILLEGAL = 2003;

    int POSTGRESQL_ERROR = 3000;
    int MONGO_ERROR = 3001;
    int REDIS_ERROR = 3002;
    int RABBIT_ERROR = 3003;

    int SCHEDULER_ERROR = 4000;
    int CACHING_ERROR = 4001;

    int UID_ERROR = 5000;

}
