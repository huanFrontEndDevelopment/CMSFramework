package tv.huan.cms.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Project Name:BasicCMS
 * File Name:UserRole
 *
 * @author wangyuxi
 * @date 2018/6/6 下午4:58
 * Copyright (c) 2016, wangyuxi@huan.tv All Rights Reserved.
 */
public class UserRole implements Serializable {
    @Getter
    @Setter
    private Integer id;
    @Getter
    @Setter
    private User user;
    @Getter
    @Setter
    private Role role;

    public UserRole(Integer id, User user, Role role) {
        this.id = id;
        this.user = user;
        this.role = role;
    }

    public UserRole() {
        super();
    }
}
