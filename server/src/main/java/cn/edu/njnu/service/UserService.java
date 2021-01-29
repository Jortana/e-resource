package cn.edu.njnu.service;

import cn.edu.njnu.mapper.UserMapper;
import cn.edu.njnu.pojo.Result;
import cn.edu.njnu.pojo.ResultFactory;
import cn.edu.njnu.pojo.User;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public User getByName(String username) {
        return userMapper.queryUserByName(username);
    }

    public User getByEmail(String userEmail) {
        return userMapper.queryUserByEmail(userEmail);
    }

    public boolean isExist(String username, String userEmail) {
        User user = getByName(username);
        if (user == null) {
            user = getByEmail(userEmail);
        }
        return null != user;
    }

    public void addUser(User user) {
        userMapper.addUser(user);
    }

    public void updateUser(User user) {
        userMapper.updateUser(user);
    }

    public boolean register(User user) {
        boolean exist = isExist(user.getUsername(), user.getUserEmail());
        if (exist) {
            return false;
        }

        HashMap<String, String> securityInfo = generateSaltAndPassword(user.getUserPassword());
        String salt = securityInfo.get("salt");
        String encodedPassword = securityInfo.get("password");
        // 储存用户信息
        user.setSalt(salt);
        user.setUserPassword(encodedPassword);
        user.setUserType(0);
        addUser(user);

        return true;
    }

    public HashMap<String, String> generateSaltAndPassword(String password) {
        HashMap<String, String> securityInfo = new HashMap<String, String>();
        // 生成盐
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        // 设置Hash算法迭代次数
        int times = 2;
        // 得到Hash后的密码
        String encodedPassword = new SimpleHash("md5", password, salt, times).toString();
        securityInfo.put("salt", salt);
        securityInfo.put("password", encodedPassword);
        return securityInfo;
    }

    public Result modifyUserInfo(User requestUser) {
        User queryUser = getByEmail(requestUser.getUserEmail());
        if (null != queryUser && !queryUser.getUsername().equals(requestUser.getUsername())) {
            return ResultFactory.buildFailResult("邮箱已被注册");
        }

        HashMap<String, String> securityInfo = generateSaltAndPassword(requestUser.getUserPassword());
        String salt = securityInfo.get("salt");
        String encodedPassword = securityInfo.get("password");
        // 储存用户信息
        requestUser.setSalt(salt);
        requestUser.setUserPassword(encodedPassword);
        updateUser(requestUser);
        return ResultFactory.buildSuccessResult("修改成功", null);
    }
}
