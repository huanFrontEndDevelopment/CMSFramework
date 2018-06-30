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
 * File Name:Permission
 *
 * @author wangyuxi
 * @date 2018/6/6 下午4:57
 * Copyright (c) 2016, wangyuxi@huan.tv All Rights Reserved.
 */
@Entity
@Getter
@Setter
public class Permission implements Serializable {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(name = "name", nullable = false, length = 255)
    private String name;
    @Column(name = "url", nullable = false, length = 255)
    private String url;
    @Column(name = "expression", nullable = false, length = 255)
    private String expression;
    @Column(name = "pos", nullable = true)
    private Integer pos;

    public Permission(Integer id, String name, String url, String expression, Integer pos) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.expression = expression;
        this.pos = pos;
    }

    public Permission() {
        super();
    }
}
