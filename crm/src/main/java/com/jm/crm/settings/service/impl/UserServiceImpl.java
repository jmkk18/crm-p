package com.jm.crm.settings.service.impl;

import com.jm.crm.settings.mapper.UserMapper;
import com.jm.crm.settings.pojo.DicValue;
import com.jm.crm.settings.pojo.User;
import com.jm.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("userService")
public class UserServiceImpl implements UserService {
    //切记切记：业务逻辑层一定有数据访问层的对象
    @Autowired
    private UserMapper userMapper;

    @Override
    public User queryUserByLoginActAndPwd(Map<String, Object> map) {
        return userMapper.selectUserByLoginActAndPwd(map);
    }

    @Override
    public List<User> queryAllUsers() {
        return userMapper.selectAllUsers();
    }

}
