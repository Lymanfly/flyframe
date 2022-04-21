package org.lyman.sec.service;

import org.lyman.enhance.hibernate.LymanDao;
import org.lyman.exceptions.LymanErrorCode;
import org.lyman.exceptions.LymanException;
import org.lyman.sec.dto.UserAuthInfoDto;
import org.lyman.sec.dto.UserRolePermissionDto;
import org.lyman.sec.entity.User;
import org.lyman.sec.entity.UserRole;
import org.lyman.sec.service.abs.UserDao;
import org.lyman.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.List;

@Service
public class UserService implements UserDao {

    private static final String USER_ID_REGEX = "[\\w]{6,20}";

    private static final String USER_NAME_REGEX = "[\\w\\u4e00-\\u9fa5]{1,20}";

    private LymanDao dao;

    @Autowired
    public UserService(LymanDao dao) {
        this.dao = dao;
    }

    @Override
    public User getUserByPrincipal(String principal) {
        return dao.findOne(User.class, " from User where id = ?0 or phoneNum = ?0 or email = ?0", principal);
    }

    @Override
    public UserAuthInfoDto getAuthorizationsByUserId(String userId) {
        List<UserRolePermissionDto> list = dao.find(UserRolePermissionDto.class,
                "select new org.lyman.sec.dto.UserRolePermissionDto(a.id, a.phoneNum, a.email, b.roleId, c.value)"
                        + " from User a left join UserRole b on a.id = b.userId left join RolePermission c"
                        + " on b.roleId = c.roleId where a.id = ?0 order by b.roleId,c.value", userId);
        if (CollectionUtils.isEmpty(list))
            return null;
        UserAuthInfoDto infoDto = new UserAuthInfoDto();
        infoDto.setUserId(userId);
        for (UserRolePermissionDto dto : list) {
            infoDto.addPrincipal(dto.getPhoneNum());
            infoDto.addPrincipal(dto.getEmail());
            infoDto.addRole(dto.getRoleId());
            infoDto.addPermission(dto.getPermission());
        }
        return infoDto;
    }

    @Override
    @Transactional
    public void userRegister(User user) {
        if (user != null) {
            try {
                dao.save(user);
            } catch (Exception e) {
                throw new LymanException(LymanErrorCode.REQUEST_ILLEGAL, "手机号或邮箱已注册，请直接登陆");
            }
        }
    }

    @Override
    @Transactional
    public void addUserRole(String userId, String roleId) {
        long count = dao.count("from UserRole where userId = ?0 and roleId = ?1", userId, roleId);
        if (count == 0L) {
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            dao.save(userRole);
        }
    }

    private static void userEncrypt(User user) {
        if (user != null) {
            String password = user.getPassword(), salt = CodecUtils.generateUUID();
            int hashIterations = getHashIterations(user);
            user.setPassword(EncryptUtils.encrypt(password, salt, hashIterations));
            user.setSalt(salt);
        }
    }

    private static void userCheck(User user) {
        if (user == null)
            throw new LymanException(LymanErrorCode.ARGUMENT_MISSSING, "User entity is null.");
        String userId = user.getId();
        String userName = user.getName();
        String phoneNum = user.getPhoneNum();
        String email = user.getEmail();
        if (StringUtils.isEmpty(userId) && StringUtils.isEmpty(phoneNum) && StringUtils.isEmpty(email))
            throw new LymanException(LymanErrorCode.ARGUMENT_ILLEGAL,
                    "Please provide at least one of UserId, Phone Number or E-mial.");
        if (phoneNum != null && !FormatUtils.validPhoneNumber(phoneNum))
            throw new LymanException(LymanErrorCode.ARGUMENT_ILLEGAL,
                    "Phone number format illegal.");
        if (email != null && !FormatUtils.validEmail(email))
            throw new LymanException(LymanErrorCode.ARGUMENT_ILLEGAL,
                    "E-mail address format illegal.");
        if (StringUtils.isEmpty(userId)) {
//            userId = "user_".concat()
        }
    }

    private static int getHashIterations(User user) {
        int hashIterations = EncryptUtils.DEFAULT_HASH_ITERATIONS;
        if (user != null) {
            String phoneNum = user.getPhoneNum();
            if (StringUtils.isNotEmpty(phoneNum)) {
                int max = NumberUtils.min(4, phoneNum.length());
                String sub = phoneNum.substring(0, max);
                try {
                    hashIterations = Integer.parseInt(sub);
                } catch (NumberFormatException ignored) {}
            }
        }
        return hashIterations;
    }


}
