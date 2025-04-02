package kitra.easyweibo.dao;

import kitra.easyweibo.entity.PostImageEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface PostImageDao {
    @Insert("INSERT INTO post_image (image, post, `order`) VALUES (#{imageId}, #{postId}, #{order})")
    int insertPostImage(int imageId, int postId, int order);

    @Select("SELECT * FROM post_image WHERE post=#{postId}")
    @Results({
        @Result(property = "image.fileName", column = "image", one = @One(select = "kitra.easyweibo.dao.ImageDao.getImageFileName")),
        @Result(property = "image.id", column = "image")
    })
    List<PostImageEntity> getPostImages(int postId);

    @Delete("DELETE FROM post_image WHERE post=#{postId}")
    int deletePostImages(int postId);
}
