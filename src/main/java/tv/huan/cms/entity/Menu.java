package tv.huan.cms.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Project Name:BasicCMS
 * File Name:Menu
 *
 * @author wangyuxi
 * @date 2018/6/6 下午4:43
 * Copyright (c) 2016, wangyuxi@huan.tv All Rights Reserved.
 */
public class Menu implements Serializable {
    @Getter
    @Setter
    private Integer id;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private Integer parentId;
    @Getter
    @Setter
    private Integer level;
    @Getter
    @Setter
    private String url;
    @Getter
    @Setter
    private String icon;
    @Getter
    @Setter
    private Integer pos;
    @Getter
    @Setter
    private List<Menu> list;


    public Menu(Integer id, String name, Integer parentId, Integer level, String url, String icon, Integer pos) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.level = level;
        this.url = url;
        this.icon = icon;
        this.pos = pos;
    }

    public Menu() {
        super();
    }
}
