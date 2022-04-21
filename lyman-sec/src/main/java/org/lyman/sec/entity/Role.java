package org.lyman.sec.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "lyman_sec_role")
@DynamicInsert
@DynamicUpdate
@Data
public class Role implements Serializable {

    @Id
    @Column(name = "role_id")
    private String id;

    @Column(name = "role_name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "is_valid")
    private Boolean isValid;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "update_by")
    private String updateBy;

}
