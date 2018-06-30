package tv.huan.cms.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Project Name:BasicCMS
 * File Name:Role
 *
 * @author wangyuxi
 * @date 2018/6/6 下午4:47
 * Copyright (c) 2016, wangyuxi@huan.tv All Rights Reserved.
 */
@Entity
@Getter
@Setter
public class Role implements Serializable {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(name = "name", nullable = false, length = 255)
    private String name;
    @Column(name = "alias", nullable = true, length = 255)
    private String alias;

    public Role(Integer id, String name, String alias) {
        this.id = id;
        this.name = name;
        this.alias = alias;
    }

    public Role() {
        super();
    }
}
