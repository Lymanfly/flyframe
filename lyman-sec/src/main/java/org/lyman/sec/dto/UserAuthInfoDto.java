package org.lyman.sec.dto;

import lombok.Getter;
import org.lyman.utils.StringUtils;

import java.util.HashSet;
import java.util.Set;

@Getter
public class UserAuthInfoDto {

    private String userId;

    private Set<String> principals = new HashSet<>();

    private Set<String> roles = new HashSet<>();

    private Set<String> permissions = new HashSet<>();

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void addRole(String roleId) {
        if (StringUtils.isNotEmpty(roleId))
            this.roles.add(roleId);
    }

    public void addPrincipal(String principal) {
        if (StringUtils.isNotEmpty(principal))
            this.principals.add(principal);
    }

    public void addPermission(String permission) {
        if (StringUtils.isNotEmpty(permission))
            this.permissions.add(permission);
    }

}
