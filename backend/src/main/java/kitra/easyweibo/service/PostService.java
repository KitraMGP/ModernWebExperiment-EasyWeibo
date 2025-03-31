package kitra.easyweibo.service;

import kitra.easyweibo.dao.PostDao;
import kitra.easyweibo.dao.UserDao;
import kitra.easyweibo.entity.PostEntity;
import kitra.easyweibo.entity.UserEntity;
import kitra.easyweibo.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    private final UserDao userDao;
    private final PostDao postDao;

    public static final int POST_INVALID_USER = -1;
    public static final int POST_TOO_LONG = -2;
    public static final int POST_UNKNOWN_ERROR = -3;

    public PostService(UserDao userDao, PostDao postDao) {
        this.userDao = userDao;
        this.postDao = postDao;
    }

    /**
     * 获取最新的帖子列表，只会加载最新的若干条帖子
     * @param limit 获取帖子个数限制
     */
    public List<PostEntity> getLatestPosts(int limit) {
        return postDao.getLatestPosts(limit);
    }

    /**
     * 获取更多帖子
     * @param lastId 上一次获取的帖子中最小（发布时间最早）的id
     * @param limit 获取帖子个数限制
     */
    public List<PostEntity> getMorePosts(int lastId, int limit) {
        return postDao.getMorePosts(lastId, limit);
    }

    /**
     * 发布帖子
     * @param user 用户名
     * @param content 内容
     * @return 帖子id，若发布失败则为负数
     */
    public int newPost(String user, String content) {
        UserEntity userEntity = userDao.getUserByName(user);
        // 用户不存在
        if (userEntity == null) {
            throw new UserNotFoundException();
        }
        // 帖子超长
        if(content.length() > 140) {
            return POST_TOO_LONG;
        }
        long time = System.currentTimeMillis() / 1000;
        PostEntity postEntity = new PostEntity();
        postEntity.setUser(userEntity);
        postEntity.setContent(content);
        postEntity.setTime(time);
        int result = postDao.insertPost(postEntity);
        if (result != 1) {
            return POST_UNKNOWN_ERROR;
        }
        // Mybatis插入postEntity数据后，会填充id属性
        return postEntity.getId();
    }
}
