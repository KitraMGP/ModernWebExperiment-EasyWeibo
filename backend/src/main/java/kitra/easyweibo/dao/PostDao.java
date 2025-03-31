package kitra.easyweibo.dao;

import kitra.easyweibo.entity.PostEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface PostDao {
    @Insert("INSERT INTO post (time, user, content, comments, likes) VALUES (#{time}, #{user.id}, #{content}, 0, 0)")
    @Options(useGeneratedKeys = true, keyProperty = "id") // 将插入数据的id填充进传入的UserEntity实例
    int insertPost(PostEntity post);

    @Delete("DELETE FROM post WHERE id = #{id}")
    int deletePost(int id);

    @Select("SELECT * FROM post ORDER BY id DESC LIMIT #{limit}")
    @Results({
        @Result(property = "user", column = "user", one = @One(select = "kitra.easyweibo.dao.UserDao.getUserById"))
    })
    List<PostEntity> getLatestPosts(int limit);

    @Select("SELECT * FROM post WHERE id < #{lastId} ORDER BY id DESC LIMIT #{limit}")
    @Results({
        @Result(property = "user", column = "user", one = @One(select = "kitra.easyweibo.dao.UserDao.getUserById"))
    })
    List<PostEntity> getMorePosts(int lastId, int limit);
}
