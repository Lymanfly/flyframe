package org.lyman.sec.user.service;

import org.lyman.enhance.hibernate.LymanDao;
import org.lyman.sec.user.entity.User;
import org.lyman.utils.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
public class UserService {

    private LymanDao dao;

    @Autowired
    public UserService(LymanDao dao) {
        this.dao = dao;
    }

    @Transactional
    public void saveUser(User user) {
        if (user == null)
            return;
        dao.save(user);
    }

    @Transactional
    public void saveUsers(Collection<User> users) {
        if (CollectionUtils.isNotEmpty(users)) dao.saveInBatch(users);
    }

    public List<User> findUsers() {
        String[] names = new String[] {
                "曼文","乐菱","痴珊","恨玉","惜文","香寒","新柔","语蓉","海安","夜蓉",
                "涵柏","水桃","醉蓝","春儿","语琴","从彤"
        };
        return dao.find(User.class, " from User where isValid is ?0 and name in ?1", Boolean.TRUE, names);
    }

    public List<User> findUsers(int limit, int offset) {
        return dao.findLimitOffset(User.class, " from User order by convert_gbk(name)",limit, offset);
    }

}
