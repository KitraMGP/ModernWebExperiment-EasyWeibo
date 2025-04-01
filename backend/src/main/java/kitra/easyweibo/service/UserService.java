package kitra.easyweibo.service;

import kitra.easyweibo.dao.UserDao;
import kitra.easyweibo.entity.UserEntity;
import kitra.easyweibo.exception.*;
import kitra.easyweibo.util.UserInfoUtil;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 提供用户注册、登录功能
 */
@Service
@Transactional
public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * 验证给定的用户名和密码是否正确
     *
     * @return 返回UserEntity
     */
    public UserEntity login(String username, String password) {
        UserEntity userEntity = userDao.getUserByName(username);
        // 用户不存在
        if (userEntity == null) {
            throw new UserNotFoundException();
        }
        if (!BCrypt.checkpw(password, userEntity.getPassword())) {
            throw new LoginFailedException();
        }
        return userEntity;
    }

    /**
     * 用户注册
     */
    public void register(String username, String nickname, String password) {
        UserEntity userEntity = userDao.getUserByName(username);
        // 用户已存在
        if (userEntity != null) {
            throw new UserAlreadyExistsException();
        }
        // 对输入进行检验
        if (!UserInfoUtil.validateNickname(nickname)) {
            throw new BadInputException("昵称");
        }
        if (!UserInfoUtil.validateUsername(username)) {
            throw new BadInputException("用户名");
        }
        if (!UserInfoUtil.validatePassword(password)) {
            throw new BadInputException("密码");
        }
        // 加密密码并插入数据库
        String encryptedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setNickname(nickname);
        user.setPassword(encryptedPassword);
        checkResult(userDao.insertUser(user));
    }

    /**
     * 更新用户信息（不包含密码和头像）
     */
    public void updateUserInfo(int userId, String newUserName, String nickname, String description) {
        // 检测用户是否存在
        UserEntity userEntity = userDao.getUserById(userId);
        if (userEntity == null) {
            throw new UserNotFoundException();
        }
        // 检测输入是否合法
        if (!UserInfoUtil.validateUsername(newUserName) || !UserInfoUtil.validateNickname(nickname) || !UserInfoUtil.validateDescription(description)) {
            throw new BadInputException("");
        }
        String oldUserName = userEntity.getUsername();
        // 如果更改了用户名
        if (!oldUserName.equals(newUserName)) {
            // 检测新用户名是否和别人重复
            if (userDao.getUserByName(newUserName) != null) {
                throw new UserAlreadyExistsException();
            }
            userEntity.setUsername(newUserName);
        }
        // 继续更新昵称和描述
        userEntity.setNickname(nickname);
        userEntity.setDescription(description);
        checkResult(userDao.updateUser(userEntity));
    }

    /**
     * 更改用户密码
     */
    public void changePassword(int userId, String oldPassword, String newPassword) {
        UserEntity userEntity = userDao.getUserById(userId);
        if (userEntity == null) {
            throw new UserNotFoundException();
        }
        if (!BCrypt.checkpw(oldPassword, userEntity.getPassword())) {
            throw new BadPasswordException();
        }
        if (!UserInfoUtil.validatePassword(newPassword)) {
            throw new BadInputException("密码");
        }
        String encryptedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        userEntity.setPassword(encryptedPassword);
        checkResult(userDao.updateUser(userEntity));
    }

    /**
     * 获取用户公开信息
     */
    public UserEntity getUserInfo(String username) {
        UserEntity userEntity = userDao.getUserByName(username);
        if (userEntity == null) {
            throw new UserNotFoundException();
        }
        return userEntity;
    }

    /**
     * 根据数字id获取用户公开信息
     */
    public UserEntity getUserInfo(int userId) {
        UserEntity userEntity = userDao.getUserById(userId);
        if (userEntity == null) {
            throw new UserNotFoundException();
        }
        return userEntity;
    }

    /**
     * 检查数据操作方法返回值是否为1，若不是1则抛出{@link DatabaseOperationException}
     */
    private void checkResult(int result) {
        if (result != 1) {
            throw new DatabaseOperationException();
        }
    }
}
