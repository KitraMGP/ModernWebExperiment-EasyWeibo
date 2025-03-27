package kitra.easyweibo.service;

import kitra.easyweibo.dao.UserDao;
import kitra.easyweibo.entity.UserEntity;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

/**
 * 提供用户注册、登录功能
 */
@Service
public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     *  验证给定的用户名和密码是否正确
     *
     * @return 若登录成功，返回UserEntity，否则返回null
     */
    public UserEntity login(String username, String password) {
        UserEntity userEntity = userDao.getUser(username);
        // 用户不存在
        if(userEntity == null) {
            return null;
        }
        if(BCrypt.checkpw(password, userEntity.getPassword())) {
            return userEntity;
        } else {
            return null;
        }
    }

    /**
     * 用户注册
     * @return 指示注册是否成功
     */
    public boolean register(String username, String nickname, String password) {
        UserEntity userEntity = userDao.getUser(username);
        // 用户已存在
        if(userEntity != null) {
            return false;
        }
        // 对输入进行检验
        // 昵称不能为空
        if(nickname.isBlank()) {
            return false;
        }
        // 对用户名进行正则表达式判断（只能由字母或数字组成，长度1-32位）
        if(!username.matches("\\w{1,32}")) {
            return false;
        }
        // 对密码进行正则表达式判断（只能由字母或数字组成，长度8-32位）
        if(!password.matches("\\w{8,32}")) {
            return false;
        }
        // 加密密码并插入数据库
        String encryptedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        UserEntity user = new UserEntity();
        user.setName(username);
        user.setNickname(nickname);
        user.setPassword(encryptedPassword);
        int result = userDao.insertUser(user);
        return result == 1;
    }
}
