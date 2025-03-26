package kitra.easyweibo.entity;

import org.apache.ibatis.type.Alias;

@Alias("user")
public class UserEntity {
    private String name;
    private String nickname;
    // 实际上是加密后的密码字符串
    private String password;

    public UserEntity(String name, String nickname, String password) {
        this.name = name;
        this.nickname = nickname;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
