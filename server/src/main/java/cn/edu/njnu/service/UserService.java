package cn.edu.njnu.service;

import cn.edu.njnu.mapper.UserMapper;
import cn.edu.njnu.pojo.User;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.stereotype.Service;

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

    public boolean register(User user) {
        boolean exist = isExist(user.getUsername(), user.getUserEmail());
        if (exist) {
            return false;
        }

        // 生成盐
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        // 设置Hash算法迭代次数
        int times = 2;
        // 得到Hash后的密码
        String encodedPassword = new SimpleHash("md5", user.getUserPassword(), salt, times).toString();
        // 储存用户信息
        user.setSalt(salt);
        user.setUserPassword(encodedPassword);
        addUser(user);

        return true;
    }
}
