package kitra.easyweibo.service;

import kitra.easyweibo.dao.*;
import kitra.easyweibo.dto.post.CommentListItem;
import kitra.easyweibo.dto.post.PostItem;
import kitra.easyweibo.entity.CommentEntity;
import kitra.easyweibo.entity.PostEntity;
import kitra.easyweibo.entity.UserEntity;
import kitra.easyweibo.exception.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static kitra.easyweibo.util.DataBaseUtil.checkResult;

@Service
@Transactional
public class PostService {
    private static final int POST_MAX_LENGTH = 140;
    private static final int COMMENT_MAX_LENGTH = 140;
    public static final int POST_IMAGES_MAX_COUNT = 9;
    private final UserDao userDao;
    private final PostDao postDao;
    private final LikeDao likeDao;
    private final CommentDao commentDao;
    private final ImageService imageService;
    private final PostImageDao postImageDao;

    public PostService(UserDao userDao, PostDao postDao, LikeDao likeDao, CommentDao commentDao, ImageService imageService, PostImageDao postImageDao) {
        this.userDao = userDao;
        this.postDao = postDao;
        this.likeDao = likeDao;
        this.commentDao = commentDao;
        this.imageService = imageService;
        this.postImageDao = postImageDao;
    }

    /**
     * 获取最新的帖子列表，只会加载最新的若干条帖子
     *
     * @param limit  获取帖子个数限制
     * @param userId 用户登录的账户id，-1代表未登录
     */
    public List<PostItem> getLatestPosts(int limit, int userId) {
        return postEntitiesToItems(postDao.getLatestPosts(limit), userId);
    }

    /**
     * 获取更多帖子
     *
     * @param lastId 上一次获取的帖子中最小（发布时间最早）的id
     * @param limit  获取帖子个数限制
     * @param userId 用户登录的账户id，-1代表未登录
     */
    public List<PostItem> getMorePosts(int lastId, int limit, int userId) {
        return postEntitiesToItems(postDao.getMorePosts(lastId, limit), userId);
    }

    /**
     * 将PostEntity列表转换为PostItem列表，查询所有帖子附带的图片，并插入PostItem中
     */
    private List<PostItem> postEntitiesToItems(List<PostEntity> postEntityList, int userId) {
        List<PostItem> postItems = new ArrayList<>(postEntityList.size());
        for (PostEntity p : postEntityList) {
            List<String> imageFileNames = new ArrayList<>(postEntityList.size());
            // 遍历图片列表，将每个图片的文件名存入列表
            postImageDao.getPostImages(p.getId()).forEach(image -> imageFileNames.add(image.getImage().getFileName()));
            // 获取帖子是否被点赞
            boolean isLike;
            if (userId == -1) {
                isLike = false;
            } else {
                isLike = likeDao.checkLike(p.getId(), userId);
            }
            // 构建PostItem列表
            if (p.getUser() != null) {
                postItems.add(new PostItem(p.getId(), p.getUser().getId(), p.getUser().getUsername(), p.getUser().getNickname(), p.getUser().hasAvatar(), p.getTime(), p.getContent(), imageFileNames.toArray(new String[0]), p.getLikes(), p.getComments(), isLike));
            } else {
                postItems.add(new PostItem(p.getId(), null, "用户已注销", "用户已注销", false, p.getTime(), p.getContent(), imageFileNames.toArray(new String[0]), p.getLikes(), p.getComments(), isLike));
            }
        }
        return postItems;
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
        if (images.length > POST_IMAGES_MAX_COUNT) {
            throw new PostImageCountExceedsLimitException();
        }
        long time = System.currentTimeMillis() / 1000;
        PostEntity postEntity = new PostEntity();
        postEntity.setUser(userEntity);
        postEntity.setContent(content);
        postEntity.setTime(time);
        checkResult(postDao.insertPost(postEntity));
        imageService.setPostImage(postEntity.getId(), images);
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
        if (images.length > POST_IMAGES_MAX_COUNT) {
            throw new PostImageCountExceedsLimitException();
        }
        long time = System.currentTimeMillis() / 1000;
        postEntity.setContent(content);
        postEntity.setTime(time);
        checkResult(postDao.updatePost(postEntity));
        imageService.setPostImage(postId, images);
    }

    /**
     * 删除帖子
     */
    public void deletePost(int userId, int postId) {
        UserEntity userEntity = userDao.getUserById(userId); // 发出删帖请求的用户
        PostEntity postEntity = postDao.getPostById(postId);
        if (userEntity == null) {
            throw new UserNotFoundException();
        }
        if (postEntity == null) {
            throw new PostNotFoundException();
        }
        // 若为管理员，则可以直接删除帖子
        if (!userEntity.isAdmin()) {
            // 注意：若帖子的user为null，这里也会提示权限不足
            if (postEntity.getUser() == null || userId != postEntity.getUser().getId()) {
                throw new PermissionDeniedException();
            }
        }
        // 删除帖子的所有图片
        imageService.setPostImage(postId, new String[0]);
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
        // 管理员可以无视权限删除评论
        if (!userEntity.isAdmin()) {
            // 可以删除自己的评论，或者自己发布的帖子的评论
            // 发送评论的用户已注销（也说明不是自己发的评论）
            if (commentEntity.getUser() == null) {
                // 发送帖子的账户已注销
                if (commentEntity.getPost().getUser() == null) {
                    throw new PermissionDeniedException();
                }
                // 不是自己发送的帖子
                if (commentEntity.getPost().getUser().getId() != userEntity.getId()) {
                    throw new PermissionDeniedException();
                }
            } else {
                // 发送评论的用户存在
                // 发送帖子的账户未注销
                if (commentEntity.getPost().getUser() != null) {
                    // 不是自己发送的评论，也不是自己发送的帖子的评论
                    if (commentEntity.getUser().getId() != userEntity.getId() && commentEntity.getPost().getUser().getId() != userEntity.getId()) {
                        throw new PermissionDeniedException();
                    }
                } else {
                    // 发送帖子的账户已注销
                    // 不是自己发送的评论
                    if (commentEntity.getUser().getId() != userEntity.getId()) {
                        throw new PermissionDeniedException();
                    }
                }
            }
        }
        checkResult(commentDao.deleteComment(commentId));
        updateCommentCount(commentEntity.getPost().getId());
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
            commentListItems[i] = new CommentListItem(comment.getId(), user.getId(), user.getUsername(), user.getNickname(), user.hasAvatar(), comment.getTime(), comment.getContent());
        }
        return commentListItems;
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
