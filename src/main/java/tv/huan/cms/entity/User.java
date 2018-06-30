package tv.huan.cms.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Project Name:BasicCMS
 * File Name:User
 *
 * @author wangyuxi
 * @date 2018/6/6 下午4:47
 * Copyright (c) 2016, wangyuxi@huan.tv All Rights Reserved.
 */
public class User implements Serializable {
    @Getter
    @Setter
    private Integer id;
    @Getter
    @Setter
    private String userName;
    @Getter
    @Setter
    private String trueName;
    @Getter
    @Setter
    private String password;
    @Getter
    @Setter
    private Integer isDelete;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Getter
    @Setter
    private Date createDate;
    @Getter
    @Setter
    private String credentialsSalt;

    public User(Integer id, String userName, String trueName, String password, Integer isdelete, Date createDate, String salt) {
        this.id = id;
        this.userName = userName;
        this.trueName = trueName;
        this.password = password;
        this.isDelete = isdelete;
        this.createDate = createDate;
        this.credentialsSalt = salt;
    }

    public User(Integer id, String userName, String trueName, Integer isdelete, Date createDate) {
        this.id = id;
        this.userName = userName;
        this.trueName = trueName;
        this.isDelete = isdelete;
        this.createDate = createDate;
    }

    public User() {
        super();
    }


}
