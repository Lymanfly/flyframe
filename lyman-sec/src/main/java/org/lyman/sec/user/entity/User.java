package org.lyman.sec.user.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "lyman_sec_user")
@DynamicInsert
@DynamicUpdate
@Data
public class User implements Serializable {

    private static final long serialVersionUID = -5804556661468065429L;

    @Id
    @Column(name = "user_id")
    private String id;

    @NotNull(message = "User's name is neccessary")
    @Column(name = "user_name")
    private String name;

    @Column(name = "is_valid")
    private Boolean isValid;

    @Column(name = "phone_num")
    private String phoneNum;

    @Column(name = "email")
    private String email;

    @Column(name = "gender")
    private Integer gender;

    @Column(name = "user_age")
    private Integer age;

    @Column(name = "loc_province")
    private String locProvince;

    @Column(name = "loc_city")
    private String locCity;

}
