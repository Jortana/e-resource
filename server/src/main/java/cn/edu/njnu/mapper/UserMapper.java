package cn.edu.njnu.mapper;

import cn.edu.njnu.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {
    // 根据用户名查找用户
    User queryUserByName(String username);
    // 根据邮箱查找用户
    User queryUserByEmail(String userEmail);
    // 添加用户
    void addUser(User user);
    // 更新用户信息
    void updateUser(User user);
}
