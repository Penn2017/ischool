package com.imis.jxufe.base.model;

import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "ischool_users")
public class IschoolUser implements Serializable{
    private Integer id;

    private String email;

    private String name;

    private String passwd;

    private String image;

    private Date createTime;

    private Date modifyTime;

    private String classId;

    private Integer state;

    private Integer type;

    public IschoolUser(){
        /***
         * empty
         */
    }

    public IschoolUser(String email) {
        this.email = email;
    }

    public IschoolUser(String email, String name, String encodingPasswd, Date now, Date now1) {
        this.email = email;
        this.name = name;
        this.passwd = encodingPasswd;
        this.createTime = now;
        this.modifyTime = now;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId == null ? null : classId.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}