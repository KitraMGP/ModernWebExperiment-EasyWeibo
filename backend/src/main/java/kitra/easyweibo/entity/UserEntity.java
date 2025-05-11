package kitra.easyweibo.entity;

import org.apache.ibatis.type.Alias;

@Alias("user")
public class UserEntity {
    private int id;
    private String username;
    private String nickname;
    private String description;
    private boolean hasAvatar;
    // 实际上是加密后的密码字符串
    private String password;
    // 用户权限，有 admin 和 user 两种。用 isAdmin() 方法来判断用户权限
    private String role;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean hasAvatar() {
        return hasAvatar;
    }

    public void setHasAvatar(boolean hasAvatar) {
        this.hasAvatar = hasAvatar;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    /**
     * 用于判断该用户是否为管理员用户。不要使用 getRole() 手动判断，应该使用此方法。
     * @return 若为管理员，则返回 true
     */
    public boolean isAdmin() {
        return role.equals("admin");
    }
}
