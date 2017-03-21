package com.imis.jxufe.user.model;

import javax.persistence.Table;

/**
 * @author zhongping
 * @date 2017/3/21
 */

@Table(name = "users")
public class User {
    private  Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
