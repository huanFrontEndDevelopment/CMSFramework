package tv.huan.cms.shiro;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import tv.huan.cms.entity.User;

/**
 * Project Name:BasicCMS
 * File Name:PasswordHelper
 * Created by wangyuxi on 2018/6/6 下午5:59.
 * Copyright (c) 2016, wangyuxi@huan.tv All Rights Reserved.
 */
public class PasswordHelper {
    private RandomNumberGenerator randomNumberGenerator;
    private String algorithmName;
    private final int hashIterations;

    public PasswordHelper() {
        randomNumberGenerator = new SecureRandomNumberGenerator();
        algorithmName = "md5";
        hashIterations = 2;
    }

    public void encryptPassword(User user){
        user.setCredentialsSalt(randomNumberGenerator.nextBytes().toHex());
        String newPassword = new SimpleHash(algorithmName,user.getPassword(), ByteSource.Util.bytes(user.getCredentialsSalt()),hashIterations).toHex();
        user.setPassword(newPassword);
    }


}
