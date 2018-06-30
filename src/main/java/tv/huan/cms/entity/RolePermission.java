package tv.huan.cms.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Project Name:BasicCMS
 * File Name:RolePermission
 *
 * @author wangyuxi
 * @date 2018/6/6 下午4:59
 * Copyright (c) 2016, wangyuxi@huan.tv All Rights Reserved.
 */
@Entity
@Getter
@Setter
public class RolePermission implements Serializable {
    @Id
    @GeneratedValue
    private Integer id;


    @OneToOne
    @JoinColumn(name = "role_id")
    private Role role;
    @OneToOne
    @JoinColumn(name = "permission_id")
    private Permission permission;

    public RolePermission(Integer id, Role role, Permission permission) {
        this.id = id;
        this.role = role;
        this.permission = permission;
    }

    public RolePermission() {
        super();
    }
}
