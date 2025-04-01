package kitra.easyweibo.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface LikeDao {
    @Insert("INSERT INTO `like` (post, user) VALUES (#{postId}, #{userId})")
    int insertLike(int postId, int userId);

    @Delete("DELETE FROM `like` WHERE post=#{postId} AND user=#{userId}")
    int deleteLike(int postId, int userId);

    @Select("SELECT COUNT(id) FROM `like` WHERE post=#{postId}")
    int countLike(int postId);

    @Select("SELECT EXISTS(SELECT id FROM `like` WHERE post=#{postId} AND user=#{userId})")
    boolean checkLike(int postId, int userId);
}
