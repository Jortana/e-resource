package cn.edu.njnu.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int userId;
    private String userEmail;
    private int userType;
    private String userPassword;
    private String username;
    private String salt;
}
