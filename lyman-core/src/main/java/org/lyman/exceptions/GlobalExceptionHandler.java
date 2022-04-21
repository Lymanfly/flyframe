package org.lyman.exceptions;

import org.lyman.response.Messenger;
import org.lyman.response.Status;
import org.springframework.core.PriorityOrdered;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler implements PriorityOrdered {

    @ExceptionHandler(LymanException.class)
    @ResponseStatus(HttpStatus.OK)
    public Messenger<String> handleCustomizedExceptions() {
        return Messenger.fail(Status.FAIL);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
