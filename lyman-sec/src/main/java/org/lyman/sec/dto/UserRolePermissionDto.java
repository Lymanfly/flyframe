package org.lyman.sec.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserRolePermissionDto {

    private String userId;

    private String phoneNum;

    private String email;

    private String roleId;

    private String permission;

}
