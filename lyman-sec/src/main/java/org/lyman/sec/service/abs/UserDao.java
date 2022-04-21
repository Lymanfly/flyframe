package org.lyman.sec.service.abs;

import org.lyman.sec.dto.UserAuthInfoDto;
import org.lyman.sec.entity.User;

public interface UserDao {

    User getUserByPrincipal(String userId);

    UserAuthInfoDto getAuthorizationsByUserId(String userId);

    void userRegister(User user);

    void addUserRole(String userId, String roleId);

}
