package com.jm.crm.settings.service;

import com.jm.crm.settings.pojo.DicValue;
import com.jm.crm.settings.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    User queryUserByLoginActAndPwd(Map<String,Object> map);

    List<User> queryAllUsers();

}
