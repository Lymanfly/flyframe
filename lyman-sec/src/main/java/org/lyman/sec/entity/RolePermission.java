package org.lyman.sec.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "lyman_sec_role_permission")
@DynamicInsert
@DynamicUpdate
@Data
public class RolePermission {

    @Id
    @Column(name = "pid")
    private long id;

    @Column(name = "role_id")
    private String role_id;

    @Column(name = "value")
    private String value;

    @Column(name = "permission_type")
    private String permissionType;

    @Column(name = "create_time")
    private Boolean createTime;

    @Column(name = "create_by")
    private Boolean createBy;

    @Column(name = "update_time")
    private Boolean updateTime;

    @Column(name = "update_by")
    private Boolean updateBy;

}
