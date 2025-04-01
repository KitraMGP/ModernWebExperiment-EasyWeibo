package kitra.easyweibo.dao;

import kitra.easyweibo.entity.CommentEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface CommentDao {
    @Insert("INSERT INTO comment (post, user, time, content) VALUES (#{post.id}, #{user.id}, #{time}, #{content})")
    @Options(useGeneratedKeys = true, keyProperty = "id") // 将插入数据的id填充进传入的CommentEntity实例
    int insertComment(CommentEntity comment);

    @Select("SELECT * FROM comment WHERE id=#{commentId}")
    @Results({
        @Result(property = "post", column = "post", one = @One(select = "kitra.easyweibo.dao.PostDao.getPostById")),
        @Result(property = "user", column = "user", one = @One(select = "kitra.easyweibo.dao.UserDao.getUserById"))
    })
    CommentEntity getCommentById(int commentId);

    @Select("SELECT * FROM comment WHERE post=#{postId}")
    @Results({
        @Result(property = "post", column = "post", one = @One(select = "kitra.easyweibo.dao.PostDao.getPostById")),
        @Result(property = "user", column = "user", one = @One(select = "kitra.easyweibo.dao.UserDao.getUserById"))
    })
    List<CommentEntity> getComments(int postId);

    @Select("SELECT COUNT(id) FROM comment WHERE post=#{postId}")
    int getCommentsCount(int postId);

    @Delete("DELETE FROM comment WHERE id=#{commentId}")
    int deleteComment(int commentId);
}
