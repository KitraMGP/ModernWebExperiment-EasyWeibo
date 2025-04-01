package kitra.easyweibo.service;

import kitra.easyweibo.dao.LikeDao;
import kitra.easyweibo.dao.PostDao;
import kitra.easyweibo.dao.UserDao;
import kitra.easyweibo.entity.PostEntity;
import kitra.easyweibo.entity.UserEntity;
import kitra.easyweibo.exception.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    private static final int POST_MAX_LENGTH = 140;
    private final UserDao userDao;
    private final PostDao postDao;
    private final LikeDao likeDao;

    public PostService(UserDao userDao, PostDao postDao, LikeDao likeDao) {
        this.userDao = userDao;
        this.postDao = postDao;
        this.likeDao = likeDao;
    }

    /**
     * 获取最新的帖子列表，只会加载最新的若干条帖子
     *
     * @param limit 获取帖子个数限制
     */
    public List<PostEntity> getLatestPosts(int limit) {
        return postDao.getLatestPosts(limit);
    }

    /**
     * 获取更多帖子
     *
     * @param lastId 上一次获取的帖子中最小（发布时间最早）的id
     * @param limit  获取帖子个数限制
     */
    public List<PostEntity> getMorePosts(int lastId, int limit) {
        return postDao.getMorePosts(lastId, limit);
    }

    /**
     * 发布帖子
     *
     * @return 帖子id
     */
    public int newPost(int userId, String content, String[] images) {
        UserEntity userEntity = userDao.getUserById(userId);
        // 用户不存在
        if (userEntity == null) {
            throw new UserNotFoundException();
        }
        // 帖子超长
        if (content.length() > POST_MAX_LENGTH) {
            throw new PostContentTooLongException();
        }
        long time = System.currentTimeMillis() / 1000;
        PostEntity postEntity = new PostEntity();
        postEntity.setUser(userEntity);
        postEntity.setContent(content);
        postEntity.setTime(time);
        int result = postDao.insertPost(postEntity);
        if (result != 1) {
            throw new DatabaseOperationException();
        }
        // TODO 添加图片
        // Mybatis插入postEntity数据后，会填充id属性
        return postEntity.getId();
    }

    /**
     * 编辑帖子
     */
    public void updatePost(int userId, int postId, String content, String[] images) {
        UserEntity userEntity = userDao.getUserById(userId);
        PostEntity postEntity = postDao.getPostById(postId);
        if (userEntity == null) {
            throw new UserNotFoundException();
        }
        if (postEntity == null) {
            throw new PostNotFoundException();
        }
        // 注意：若帖子的user为null，这里也会提示权限不足
        if (userId != postEntity.getUser().getId()) {
            throw new PermissionDeniedException();
        }
        if (content.length() > POST_MAX_LENGTH) {
            throw new PostContentTooLongException();
        }
        long time = System.currentTimeMillis() / 1000;
        postEntity.setContent(content);
        postEntity.setTime(time);
        int result = postDao.updatePost(postEntity);
        if (result != 1) {
            throw new DatabaseOperationException();
        }
        // TODO 添加图片
    }

    /**
     * 删除帖子
     */
    public void deletePost(int userId, int postId) {
        UserEntity userEntity = userDao.getUserById(userId);
        PostEntity postEntity = postDao.getPostById(postId);
        if (userEntity == null) {
            throw new UserNotFoundException();
        }
        if (postEntity == null) {
            throw new PostNotFoundException();
        }
        // 注意：若帖子的user为null，这里也会提示权限不足
        if (userId != postEntity.getUser().getId()) {
            throw new PermissionDeniedException();
        }
        int result = postDao.deletePost(postId);
        if (result != 1) {
            throw new DatabaseOperationException();
        }
    }

    /**
     * 设置帖子点赞状态
     */
    public void likePost(int userId, int postId, boolean isLike) {
        UserEntity userEntity = userDao.getUserById(userId);
        PostEntity postEntity = postDao.getPostById(postId);
        if (userEntity == null) {
            throw new UserNotFoundException();
        }
        if (postEntity == null) {
            throw new PostNotFoundException();
        }
        // 如果请求的点赞/取消点赞已经执行过了，直接返回
        if (likeDao.checkLike(postId, userId) == isLike) {
            return;
        }
        int result;
        if (isLike) result = likeDao.insertLike(postId, userId);
        else result = likeDao.deleteLike(postId, userId);
        if (result != 1) {
            throw new DatabaseOperationException();
        }
        // 重新计算赞的数量
        int likeCount = likeDao.countLike(postId);
        result = postDao.updateLikesCount(postId, likeCount);
        if (result != 1) {
            throw new DatabaseOperationException();
        }
    }
}
