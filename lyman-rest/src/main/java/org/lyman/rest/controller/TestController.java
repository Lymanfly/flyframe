package org.lyman.rest.controller;

import org.lyman.sec.user.entity.User;
import org.lyman.sec.user.service.UserService;
import org.lyman.utils.SpringContextUtils;
import org.lyman.rest.web.WebConstants;
import org.lyman.utils.CodecUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = WebConstants.API_REQUEST_PATH + "/test")
public class TestController {

    @Autowired
    private UserService userService;

    @GetMapping
    @Validated
    public void test(@RequestParam(value = "args", required = false) String args) {
        ApplicationContext context = SpringContextUtils.getApplicationContext();
        Resource resource = context.getResource("classpath:names");
        if (resource != null && resource.isReadable()) {
            List<User> users = new ArrayList<>();
            try {
                File file = resource.getFile();
                BufferedReader reader = new BufferedReader(new FileReader(file));
                while (reader.ready()) {
                    String word = reader.readLine();
                    User user = new User();
                    user.setId(CodecUtils.generateUUID());
                    user.setName(word);
                    users.add(user);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            userService.saveUsers(users);
        }
    }

}
