package org.lyman.rest.controller;

import org.lyman.config.redis.RedisUtils;
import org.lyman.response.Messenger;
import org.lyman.rest.web.WebConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = WebConstants.API_REQUEST_PATH + "/test")
public class TestController {

    @Autowired
    private RedisUtils redisUtils;

    @GetMapping
    @Validated
    public Messenger<String> test(@RequestParam(value = "args", required = false) String args) {
        redisUtils.set("request-args", args);
        return Messenger.success();
    }

}
