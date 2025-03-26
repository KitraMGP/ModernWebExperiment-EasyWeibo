package kitra.easyweibo.service.user;

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

    public boolean verifyPassword(String username, String password) {
        UserEntity userEntity = userDao.getUser(username);
        // 用户不存在
        if(userEntity == null) {
            return false;
        }
        return BCrypt.checkpw(password, userEntity.getPassword());
        // TODO 需要补单元测试
    }
}
