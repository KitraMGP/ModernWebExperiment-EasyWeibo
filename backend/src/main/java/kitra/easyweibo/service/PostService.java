package kitra.easyweibo.service;

import kitra.easyweibo.dao.CommentDao;
import kitra.easyweibo.dao.LikeDao;
import kitra.easyweibo.dao.PostDao;
import kitra.easyweibo.dao.UserDao;
import kitra.easyweibo.dto.post.CommentListItem;
import kitra.easyweibo.entity.CommentEntity;
import kitra.easyweibo.entity.PostEntity;
import kitra.easyweibo.entity.UserEntity;
import kitra.easyweibo.exception.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PostService {
    private static final int POST_MAX_LENGTH = 140;
    private static final int COMMENT_MAX_LENGTH = 140;
    private final UserDao userDao;
    private final PostDao postDao;
    private final LikeDao likeDao;
    private final CommentDao commentDao;

    public PostService(UserDao userDao, PostDao postDao, LikeDao likeDao, CommentDao commentDao) {
        this.userDao = userDao;
        this.postDao = postDao;
        this.likeDao = likeDao;
        this.commentDao = commentDao;
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
            throw new ContentTooLongException();
        }
        long time = System.currentTimeMillis() / 1000;
        PostEntity postEntity = new PostEntity();
        postEntity.setUser(userEntity);
        postEntity.setContent(content);
        postEntity.setTime(time);
        checkResult(postDao.insertPost(postEntity));
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
        if (postEntity.getUser() == null || userId != postEntity.getUser().getId()) {
            throw new PermissionDeniedException();
        }
        if (content.length() > POST_MAX_LENGTH) {
            throw new ContentTooLongException();
        }
        long time = System.currentTimeMillis() / 1000;
        postEntity.setContent(content);
        postEntity.setTime(time);
        checkResult(postDao.updatePost(postEntity));
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
        if (postEntity.getUser() == null || userId != postEntity.getUser().getId()) {
            throw new PermissionDeniedException();
        }
        checkResult(postDao.deletePost(postId));
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
        if (isLike) {
            checkResult(likeDao.insertLike(postId, userId));
        } else {
            checkResult(likeDao.deleteLike(postId, userId));
        }
        updateLikeCount(postId);
    }

    /**
     * 添加评论
     */
    public int addComment(int userId, int postId, String content) {
        UserEntity userEntity = userDao.getUserById(userId);
        PostEntity postEntity = postDao.getPostById(postId);
        if (userEntity == null) {
            throw new UserNotFoundException();
        }
        if (postEntity == null) {
            throw new PostNotFoundException();
        }
        if (content.length() > COMMENT_MAX_LENGTH) {
            throw new ContentTooLongException();
        }
        long time = System.currentTimeMillis() / 1000;
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setPost(postEntity);
        commentEntity.setUser(userEntity);
        commentEntity.setTime(time);
        commentEntity.setContent(content);
        checkResult(commentDao.insertComment(commentEntity));
        updateCommentCount(postId);
        return commentEntity.getId();
    }

    /**
     * 删除评论
     */
    public void deleteComment(int userId, int commentId) {
        UserEntity userEntity = userDao.getUserById(userId);
        CommentEntity commentEntity = commentDao.getCommentById(commentId);
        if (userEntity == null) {
            throw new UserNotFoundException();
        }
        if (commentEntity == null) {
            throw new CommentNotFoundException();
        }
        // 注意：若评论的user为null，也会提示权限不足
        if (commentEntity.getUser() == null || commentEntity.getUser().getId() != userEntity.getId()) {
            throw new PermissionDeniedException();
        }
        checkResult(commentDao.deleteComment(commentId));
    }

    /**
     * 获取帖子评论列表
     */
    public CommentListItem[] getComments(int postId) {
        if (postDao.getPostById(postId) == null) {
            throw new PostNotFoundException();
        }
        List<CommentEntity> commentEntities = commentDao.getComments(postId);
        CommentListItem[] commentListItems = new CommentListItem[commentEntities.size()];
        for (int i = 0; i < commentEntities.size(); i++) {
            CommentEntity comment = commentEntities.get(i);
            UserEntity user = comment.getUser();
            commentListItems[i] = new CommentListItem(comment.getId(), user.getUsername(), user.getNickname(), user.hasAvatar(), comment.getTime(), comment.getContent());
        }
        return commentListItems;
    }

    /**
     * 判断数据库操作返回值是否为1。若不是1，则抛出{@link DatabaseOperationException}
     *
     * @param result 数据操作方法的返回值
     */
    private void checkResult(int result) {
        if (result != 1) {
            throw new DatabaseOperationException();
        }
    }

    /**
     * 重新计算帖子点赞个数并更新数据库
     */
    private void updateLikeCount(int postId) {
        int likeCount = likeDao.countLike(postId);
        checkResult(postDao.updateLikesCount(postId, likeCount));
    }

    /**
     * 重新计算评论个数并更新数据库
     */
    private void updateCommentCount(int postId) {
        int commentCount = commentDao.getCommentsCount(postId);
        checkResult(postDao.updateCommentCount(postId, commentCount));
    }
}
