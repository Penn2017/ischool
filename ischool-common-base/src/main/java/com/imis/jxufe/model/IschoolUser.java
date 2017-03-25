package com.imis.jxufe.model;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Past;
import java.time.LocalDateTime;


@Table(name = "ischool_users")
public class IschoolUser {

    @Id
    private Integer id;

    @Email
    private String email;

    private String name;

    @NotEmpty
    private String passwd;

    private String image;

    @Past
    private LocalDateTime createTime;


    private LocalDateTime modifyTime;

    private Integer type;
    private Integer state;


    public IschoolUser(){}
    public IschoolUser(String email, String name, String passwd, LocalDateTime createTime, LocalDateTime modifyTime) {
        this.email = email;
        this.name = name;
        this.passwd = passwd;
        this.createTime = createTime;
        this.modifyTime = modifyTime;
    }

    public IschoolUser(String email) {
        this.email = email;

    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd == null ? null : passwd.trim();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}